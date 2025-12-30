# Alert Watcher (Android)

Application Android qui surveille les notifications d'une application cible (par exemple gDMSS) et déclenche une alerte sonore configurable lorsqu'un mot-clé est détecté.

## Fonctionnalités
- Sélection d'une application à surveiller (par nom de package).
- Filtrage de notifications via mot-clé (ex. "alert dvr").
- Choix d'une sonnerie ou d'un fichier audio via URI et réglage du nombre de répétitions.
- Fenêtre horaire d'activation (définie dans `AlertConfig`).
- Fonctionne en arrière-plan grâce à un `NotificationListenerService` et une simple planification (`AlertScheduler`).

## Démarrage rapide (Android Studio)
1. Installer Android Studio Giraffe (ou plus récent) avec le SDK 34.
2. Ouvrir ce dossier `android-app` via **File > Open**.
3. Brancher un appareil Android 7.0+ (API 24) avec le débogage USB activé ou démarrer un émulateur.
4. Lancer **Run > Run 'app'** pour compiler et déployer l'APK.
5. À l'ouverture, accepter la demande d'accès aux notifications pour « Alert Watcher ».
6. Renseigner le package cible (ex. `com.mm.android.direct.gdmssphone`), le mot-clé (ex. `alert dvr`), l'URI de sonnerie et le nombre de répétitions, puis sauvegarder.
7. Envoyer depuis l'application surveillée une notification contenant le mot-clé : la sonnerie configurée doit se jouer.

## Ligne de commande (sans Android Studio)
1. Installer Java 17+ et l'Android SDK (platforms;android-34, build-tools 34.x).
2. Dans ce dossier, exécuter :
   ```bash
   ./gradlew assembleDebug
   ```
   L'APK est généré dans `app/build/outputs/apk/debug/app-debug.apk`.
3. Installer l'APK sur un appareil ou émulateur connecté :
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
4. Ouvrir l'application puis autoriser l'accès aux notifications.

## Points d'attention
- L'application n'inclut pas de sélecteur graphique pour les fichiers audio; fournissez l'URI (par ex. `content://settings/system/notification_sound` ou une URI de fichier que vous possédez). 
- `AlertScheduler` implémente une fenêtre horaire minimale dans `AlertConfig`; adaptez si besoin via WorkManager ou AlarmManager pour des scénarios plus avancés.
- Désactivez ou ajustez les restrictions d'arrière-plan / batterie (Paramètres système) pour garantir que le `NotificationListenerService` reste actif.
