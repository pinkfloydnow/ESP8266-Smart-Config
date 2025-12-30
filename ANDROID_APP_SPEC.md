# Spécification d'application Android de surveillance de notifications

## Résumé
Cette application Android tourne en arrière-plan pour écouter les notifications d'une application choisie (p. ex. GDMSS). Lorsqu'un texte d'alerte (comme « alert dvr ») est détecté, elle déclenche une action configurable : lecture de sonneries personnalisées, répétition et planification temporelle.

## Fonctionnalités principales
- **Sélection de la source** : liste des applications installées ; l'utilisateur choisit celle dont les notifications seront surveillées.
- **Filtrage de contenu** : champ texte pour définir les mots-clés (par défaut « alert dvr ») ; correspondance insensible à la casse et aux accents.
- **Choix de l'action** :
  - Sélection d'une sonnerie ou d'un fichier audio local.
  - Définition du nombre de répétitions et de la durée entre répétitions.
- **Planification** :
  - Jours actifs sélectionnables (lun-dim).
  - Plages horaires actives (p. ex. 22h–06h) ; prise en charge de plusieurs créneaux.
- **Bascule globale** : activer/désactiver la surveillance sans désinstaller l'application.
- **Persistance** : stockage local (SharedPreferences ou DataStore) des règles, sonneries et horaires.
- **Exécution en arrière-plan** : service Foreground + NotificationListenerService pour rester autorisé par Android.
- **Compatibilité** : viser Android 8.0 (API 26) minimum pour la gestion stricte des services en arrière-plan.

## Architecture proposée
- **Module core** :
  - Gestion des règles de déclenchement (app source, mots-clés, jours, plages horaires, répétitions).
  - Orchestrateur d'actions (lecture audio via `MediaPlayer`/`ExoPlayer`).
- **Services** :
  - `AlertNotificationListener` : hérite de `NotificationListenerService`, filtre les notifications entrantes de l'app choisie, extrait titre/corps, applique le filtre texte.
  - `Scheduler` : utilise `WorkManager` ou `AlarmManager` pour activer/désactiver la surveillance selon les plages configurées.
  - `ForegroundMonitoringService` : garde l'application vivante avec une notification persistante.
- **UI** :
  - Écran de sélection d'application (liste des packages).
  - Écran de configuration des mots-clés.
  - Écran de choix de sonnerie (picker de fichiers audio) et des répétitions.
  - Écran de planification (jours + plages horaires).
  - Bascule globale + test manuel (« jouer maintenant »).

## Flux principal
1. Au premier lancement, demander l'autorisation d'accès aux notifications et l'exclusion des optimisations batterie.
2. L'utilisateur sélectionne l'application à surveiller et configure les mots-clés.
3. L'utilisateur choisit la sonnerie/fichier audio, le nombre de répétitions et le délai entre répétitions.
4. L'utilisateur définit les jours et plages horaires actifs.
5. Le service de surveillance tourne en foreground ; à chaque notification de l'app source, le texte est analysé :
   - si le texte contient un mot-clé et que l'on est dans une plage active, l'action est déclenchée (lecture audio répétée selon réglages).

## Sécurité et résilience
- Gérer la perte d'autorisation `Notification Listener` (rediriger vers les paramètres si révoqué).
- Vérifier les permissions de lecture de stockage si la sonnerie provient d'un fichier externe.
- Gérer les redémarrages d'appareil via `BOOT_COMPLETED` pour relancer le service et le scheduler.
- Prévoir un canal de notification dédié pour le service foreground (Android 8+).

## Tests recommandés
- Unitaire : parsing des notifications, filtrage des mots-clés, validation des plages horaires.
- Intégration : service de notification, lecture audio, gestion des répétitions et pauses.
- E2E manuel : réception de notifications réelles GDMSS/DVR, validation des règles et du planning.
