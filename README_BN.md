# JARVIS Android — ফোন থেকে APK বানানোর প্রাথমিক প্রজেক্ট

এটি একটি **starter version**। এতে আছে:
- বোতাম চেপে বাংলা/ইংরেজি voice command বলা
- YouTube, Facebook, Chrome, Play Store ও Settings খোলা
- সময় বলা এবং Text-to-Speech উত্তর

## গুরুত্বপূর্ণ সীমাবদ্ধতা
- এটি এখনো সবসময় “Jarvis” wake word শুনে না। আপাতত **TAP TO TALK** চাপতে হবে।
- সবসময় শোনার wake-word ফিচারের জন্য আলাদা wake-word engine, model/key, foreground microphone service এবং Android permission setup লাগবে।
- এটি ChatGPT/অনলাইন AI-তে যুক্ত নয়; তাই সাধারণ প্রশ্নের উত্তর দিতে পারে না।
- Speech recognition-এর জন্য ফোনে Google speech recognition service/ইন্টারনেট লাগতে পারে।
- মাইক্রোফোন ব্যবহারের আগে Android permission চাইবে।

## GitHub দিয়ে APK বানানোর নিয়ম (মোবাইল)
1. GitHub-এ sign in করে **New repository** তৈরি করো। নাম দাও `JarvisAndroid`।
2. এই ZIP extract করে ভেতরের সব ফাইল repository-তে upload করো। `JarvisAndroid` ফোল্ডারটিকে আবার ভেতরে nested করে দেবে না—`app`, `.github`, `build.gradle.kts` সরাসরি repository root-এ থাকবে।
3. GitHub repository-তে **Actions** tab খোলো।
4. `Build JARVIS APK` workflow নির্বাচন করে **Run workflow** চাপো।
5. কাজ শেষ হলে workflow run খুলে নিচে **Artifacts** থেকে `jarvis-debug-apk` ডাউনলোড করো।
6. ZIP extract করে `app-debug.apk` ইনস্টল করো। Android চাইলে “Install unknown apps” অনুমতি দিতে হবে।

## লোকাল build
Android Studio + JDK 17 + Android SDK 35 এবং Gradle 8.9 দিয়ে `gradle assembleDebug` চালানো যাবে।
