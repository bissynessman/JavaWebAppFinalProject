pyinstaller.exe --onefile --noconsole ^
--add-binary "..\bin\jwafp_handler embedded dlls\jwafp_library.dll;." ^
--add-binary "..\bin\jwafp_handler embedded dlls\libcurl.dll;." ^
--workpath "..\build artifacts\PyInstaller" ^
--distpath "..\bin" ^
"..\source\jwafp_handler.py"