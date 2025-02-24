// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.diagnostic;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AnimatedIcon.Blinking;

import javax.swing.*;
import java.awt.*;

import static com.intellij.util.ui.EmptyIcon.ICON_16;

final class IdeErrorsIcon extends JLabel {
  private final boolean myEnableBlink;

  IdeErrorsIcon(boolean enableBlink) {
    myEnableBlink = enableBlink && !Boolean.getBoolean("fatal.error.icon.disable.blinking");
  }

  void setState(MessagePool.State state) {
    Icon myUnreadIcon = !myEnableBlink ? AllIcons.Ide.FatalError : new Blinking(AllIcons.Ide.FatalError);
    if (state != null && state != MessagePool.State.NoErrors) {
      setIcon(state == MessagePool.State.ReadErrors ? AllIcons.Ide.FatalErrorRead : myUnreadIcon);
      setToolTipText(DiagnosticBundle.message("error.notification.tooltip"));
      getAccessibleContext().setAccessibleDescription(StringUtil.removeHtmlTags(DiagnosticBundle.message("error.notification.tooltip")));
      if (!myEnableBlink) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
    }
    else {
      setIcon(ICON_16);
      setToolTipText(null);
      if (!myEnableBlink) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    }
  }
}
