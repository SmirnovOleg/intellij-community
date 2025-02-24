// "Mark 'source' as safe" "false"
package org.checkerframework.checker.tainting.qual;

class Simple {

  void simple() {
    sink(<caret>source());
  }

  String foo() {
    return "foo";
  }
  
  @Tainted String source() {
    return "source";
  }

  void sink(@Untainted String s1) {}

}