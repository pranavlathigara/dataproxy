# DataProxy
Library that provide easy way make a comunication between remote application (such wear and phone application, processes, etc) using a common interface.

# Usage

  Create a common interface with void method.
  ```java
  public interface TestInterface {

    void onTestNoParameters();
    void onTestPrimitive(String s, int i, long l, float f, double d, short st, char c, boolean b, byte bt);
    void onTestSerializable(File file);
    void onTestParcelable(Intent intent);
    ...
  }
  ```
 
  Get mocked interface by passing the interface class and a sender, then call any method in this interface.
  ```java
  void callNoParams() {
      final TestInterface test = DataProxy.get().build(TestInterface.class, new BroadcastSender(this))
      test.onTestNoParameters();
  }
  ```

  The callback `onTestNoParameters()` of any registered object that implements this interface will be called.
  
### Supportable data
  * Primitives
  * Serializables
  * Parcelables

# Compatibility
Android API 1 or above.

# License

    Copyright 2015, fabriciovergal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
