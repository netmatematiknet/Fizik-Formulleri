# Fizik Formülleri

Android uygulaması: YKS / TYT (ve AYT altyapısı) fizik formül görselleri.

- Paket: `com.mobilprogramlar.FizikFormullerim`
- Sürüm: 2.0 (`versionCode` 10)
- Hedef API: 36

## Özellikler

- TYT konu listesi, arama, favoriler
- AYT filtresi (içerik eklendikçe `TopicCatalog.ayt()`)
- Tablet iki panel (liste + formül)
- Firebase: Analytics, Crashlytics, Performance, Messaging, Remote Config
- AdMob + UMP; reklam kontrolü Remote Config üzerinden
- Material 3 / DayNight, çentik uyumu, uygulama içi güncelleme

## Derleme

Android Studio ile `FizikFormullerim` klasörünü açın veya:

```bat
gradlew.bat :app:assembleDebug
```

Release için imzalı AAB üretin ve Play Console’a yükleyin.
