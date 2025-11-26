[Setup]
AppName=JWAFP Installer
AppVersion=1.0
DefaultDirName={commonpf}\JWAFP
DisableDirPage=no
DefaultGroupName=JWAFP
OutputBaseFilename=JWAFP Installer
Compression=lzma
SolidCompression=yes

[Files]
Source: "..\bin\jwafp_handler.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "..\bin\cert.pem"; DestDir: "{app}"; Flags: ignoreversion

[Registry]
Root: HKCR; Subkey: "jwafp"; ValueType: string; ValueName: ""; ValueData: "URL:JWAFP Protocol"; Flags: uninsdeletekey
Root: HKCR; Subkey: "jwafp"; ValueType: string; ValueName: "URL Protocol"; ValueData: ""; Flags: uninsdeletekey
Root: HKCR; Subkey: "jwafp\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\jwafp_handler.exe"" ""%1"""; Flags: uninsdeletekey

[UninstallDelete]
Type: files; Name: "{app}\jwafp_handler.exe"
