C:/msys64/ucrt64/bin/g++.exe -fdiagnostics-color=always ^
-I "C:\Program Files (x86)\vcpkg\installed\x64-windows\include" ^
-L "C:\Program Files (x86)\vcpkg\installed\x64-windows\lib" ^
-g "..\source\jwafp_library.cpp" ^
-o "..\bin\jwafp_handler embedded dlls\jwafp_library.dll" ^
-lcurl -lole32 -lcomctl32 -lshell32 -luser32 -lgdi32 -ladvapi32 -lws2_32 -luuid -lssl -lcrypto ^
-shared