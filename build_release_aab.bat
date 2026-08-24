@echo off
setlocal
cd /d "%~dp0"

echo.
echo [0/4] keystore.properties kontrolu...
if not exist "keystore.properties" (
    echo.
    echo HATA: keystore.properties bulunamadi - AAB IMZASIZ olur, Play kabul etmez!
    echo.
    echo 1^) keystore.properties.example dosyasini keystore.properties olarak kopyalayin
    echo 2^) FizikFormulleri.jks sifre ve alias bilgilerini yazin
    echo    JKS: D:\AndroidStudio\FizikFormullerim\FizikFormulleri.jks
    echo.
    echo Alternatif: Android Studio ^> Build ^> Generate Signed Bundle / APK
    exit /b 1
)

echo.
echo ONEMLI: Android Studio ve baska Gradle pencerelerini KAPATIN.
echo        Ayni anda hem Studio hem bu script calisirsa classes.dex kilitlenir.
echo.
pause

echo.
echo [1/4] Gradle daemon durduruluyor...
call gradlew.bat --stop
ping 127.0.0.1 -n 4 >nul

echo.
echo [2/4] Kilitli dex/cache klasorleri temizleniyor...
if exist "app\build\intermediates\dex" (
    rmdir /s /q "app\build\intermediates\dex" 2>nul
    if exist "app\build\intermediates\dex" (
        echo.
        echo HATA: classes.dex hala kilitli. Android Studio tamamen kapali mi?
        echo Gorev Yoneticisi ^> java.exe sureclerini sonlandirin, tekrar deneyin.
        exit /b 1
    )
)
if exist "app\build\intermediates\lint_vital_intermediate_text_report" rmdir /s /q "app\build\intermediates\lint_vital_intermediate_text_report" 2>nul
if exist ".gradle\buildOutputCleanup" rmdir /s /q ".gradle\buildOutputCleanup" 2>nul

echo.
echo [3/4] Release AAB derleniyor (tek islem, dosya kilidi onlemli)...
call gradlew.bat :app:bundleRelease --no-daemon --no-parallel --rerun-tasks
set ERR=%ERRORLEVEL%

echo.
if %ERR% NEQ 0 (
    echo HATA: Derleme basarisiz. Android Studio kapali mi kontrol edin.
    echo Ayrica Build ^> Clean Project yapip tekrar deneyin.
    exit /b %ERR%
)

echo [4/4] AAB kopyalaniyor...
set SRC=app\build\outputs\bundle\release\app-release.aab
set DST=..\FizikFormulleri_v2.0_12.aab
if exist "%SRC%" (
    copy /Y "%SRC%" "%DST%" >nul
    echo BASARILI: %DST%
    echo         %CD%\%SRC%
) else (
    echo UYARI: AAB bulunamadi: %SRC%
    exit /b 1
)

endlocal
