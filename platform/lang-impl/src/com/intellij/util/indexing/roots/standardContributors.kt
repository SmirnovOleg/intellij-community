// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.util.indexing.roots

import com.intellij.ide.lightEdit.LightEdit
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.*
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.util.registry.Registry
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.AdditionalIndexableFileSet
import com.intellij.util.indexing.IndexableSetContributor
import com.intellij.util.indexing.roots.IndexableEntityProviderMethods.mergeIterators
import com.intellij.workspaceModel.ide.WorkspaceModel
import com.intellij.workspaceModel.storage.WorkspaceEntity
import com.intellij.workspaceModel.storage.WorkspaceEntityStorage
import org.jetbrains.annotations.Nls
import java.util.function.Predicate

internal class DefaultProjectIndexableFilesContributor : IndexableFilesContributor {
  override fun getIndexableFiles(project: Project): List<IndexableFilesIterator> {
    if (indexProjectBasedOnIndexableEntityProviders()) {
      val iterators: MutableList<IndexableFilesIterator> = mutableListOf()
      val entityStorage = WorkspaceModel.getInstance(project).entityStorage.current
      for (provider in IndexableEntityProvider.EP_NAME.extensionList) {
        if (provider is IndexableEntityProvider.Existing) {
          addIteratorsFromProvider(provider, entityStorage, project, iterators)
        }
      }
      return mergeIterators(iterators)
    }
    else {
      val seenLibraries: MutableSet<Library> = HashSet()
      val seenSdks: MutableSet<Sdk> = HashSet()
      val modules = ModuleManager.getInstance(project).sortedModules

      val providers: MutableList<IndexableFilesIterator> = mutableListOf()
      for (module in modules) {
        providers.addAll(ModuleIndexableFilesIteratorImpl.getModuleIterators(module))

        val orderEntries = ModuleRootManager.getInstance(module).orderEntries
        for (orderEntry in orderEntries) {
          when (orderEntry) {
            is LibraryOrderEntry -> {
              val library = orderEntry.library
              if (library != null && seenLibraries.add(library)) {
                providers.add(LibraryIndexableFilesIteratorImpl(library))
              }
            }
            is JdkOrderEntry -> {
              val sdk = orderEntry.jdk
              if (sdk != null && seenSdks.add(sdk)) {
                providers.add(SdkIndexableFilesIteratorImpl(sdk))
              }
            }
          }
        }
      }
      return providers
    }
  }

  override fun getOwnFilePredicate(project: Project): Predicate<VirtualFile> {
    val projectFileIndex: ProjectFileIndex = ProjectFileIndex.getInstance(project)

    return Predicate {
      if (LightEdit.owns(project)) {
        return@Predicate false
      }
      if (projectFileIndex.isInContent(it) || projectFileIndex.isInLibrary(it)) {
        !FileTypeManager.getInstance().isFileIgnored(it)
      }
      else false
    }
  }

  companion object {
    private fun <E : WorkspaceEntity> addIteratorsFromProvider(provider: IndexableEntityProvider.Existing<E>,
                                                               entityStorage: WorkspaceEntityStorage,
                                                               project: Project,
                                                               iterators: MutableList<IndexableFilesIterator>) {
      val entityClass = provider.entityClass
      for (entity in entityStorage.entities(entityClass)) {
        iterators.addAll(provider.getExistingEntityIterator(entity, entityStorage, project))
      }
    }

    @JvmStatic
    fun indexProjectBasedOnIndexableEntityProviders(): Boolean = Registry.`is`("indexing.enable.entity.provider.based.indexing")
  }
}

internal class AdditionalFilesContributor : IndexableFilesContributor {
  override fun getIndexableFiles(project: Project): List<IndexableFilesIterator> {
    return IndexableSetContributor.EP_NAME.extensionList.flatMap {
      listOf(IndexableSetContributorFilesIterator(it, true),
             IndexableSetContributorFilesIterator(it, false))
    }
  }

  override fun getOwnFilePredicate(project: Project): Predicate<VirtualFile> {
    val additionalFilesContributor = AdditionalIndexableFileSet(project)
    return Predicate(additionalFilesContributor::isInSet)
  }
}

internal class AdditionalLibraryRootsContributor : IndexableFilesContributor {
  override fun getIndexableFiles(project: Project): List<IndexableFilesIterator> {
    return AdditionalLibraryRootsProvider.EP_NAME
      .extensionList
      .flatMap { it.getAdditionalProjectLibraries(project) }
      .map { SyntheticLibraryIndexableFilesIteratorImpl(it) }
  }

  override fun getOwnFilePredicate(project: Project): Predicate<VirtualFile> {
    return Predicate { false }
    // todo: synthetic library changes are served in DefaultProjectIndexableFilesContributor
  }

  companion object {
    @JvmStatic
    fun createIndexingIterator(presentableLibraryName: @Nls String?, rootsToIndex: List<VirtualFile>, libraryNameForDebug: String): IndexableFilesIterator =
      AdditionalLibraryIndexableAddedFilesIterator(presentableLibraryName, rootsToIndex, libraryNameForDebug)
  }
}