$ErrorActionPreference = 'Stop'
$cache = Join-Path $env:USERPROFILE '.gradle/yuki-distributions'
New-Item -ItemType Directory -Force $cache | Out-Null
$gradle = Join-Path $cache 'gradle-8.11.1/bin/gradle.bat'
if (!(Test-Path $gradle)) {
 $zip = Join-Path $cache 'gradle.zip'
 Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-8.11.1-bin.zip' -OutFile $zip
 if ((Get-FileHash $zip -Algorithm SHA256).Hash.ToLower() -ne 'f397b287023acdba1e9f6fc5ea72d22dd63669d59ed4a289a29b1a76eee151c6') { throw 'Gradle checksum mismatch' }
 Expand-Archive $zip $cache -Force
 Remove-Item $zip
}
& $gradle @args
exit $LASTEXITCODE
