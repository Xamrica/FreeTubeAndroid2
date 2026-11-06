# FreeTubeAndroid2

> [!NOTE]
> This is a vibecoded fork of [FreeTubeAndroid](https://github.com/MarmadileManteater/FreeTubeAndroid) with some additional fixes and features.
> See the [release notes](https://github.com/Xamrica/FreeTubeAndroid2/releases) for the changes.
>
> Most of this README comes from FreeTubeAndroid.

---

<p align="center" >
 <img src="https://docs.freetubeapp.io/images/logoColor.png" width=500 align="center" alt="FreeTube Logo">
</p>
<h2 align='center'>
An open source YouTube player built with privacy in mind.
</h2>

<p align='center'>
<a href="https://hosted.weblate.org/engage/free-tube/">
<img src="https://hosted.weblate.org/widgets/free-tube/-/svg-badge.svg" alt="Translation status" />
</a>
</p>
<hr>
<p align="center"><a href="#screenshots">Screenshots</a> &bull; <a href="#how-does-it-work">How does it work?</a> &bull; <a href="#features">Features</a> &bull; <a href="#how-to-build-and-test">Building and testing</a> &bull; <a href="#localization">Localization</a> &bull; <a href="#contact">Contact</a> &bull; <a href="#donations">Donate</a> &bull; <a href="#license">License</a></p>
<p align="center"><a href="https://freetubeapp.io/">FreeTube website</a> &bull; <a href="https://blog.freetubeapp.io/">FreeTube blog</a> &bull; <a href="https://docs.freetubeapp.io/">FreeTube documentation</a> &bull; <a href="https://docs.freetubeapp.io/faq/">FreeTube FAQ</a></p>
<hr>

FreeTubeAndroid2 is an open source YouTube player built with privacy in mind. Use YouTube without advertisements and prevent Google from tracking you with their cookies and JavaScript.
Each release contains an APK. You can build a PWA (progressive web app) from source. FreeTubeAndroid2 is a fork of [FreeTubeAndroid](https://github.com/MarmadileManteater/FreeTubeAndroid), which is a fork of [FreeTube](https://www.github.com/FreeTubeApp).

<p align="center"><a href="https://github.com/Xamrica/FreeTubeAndroid2/releases/latest">Download the APK from GitHub releases</a></p>

<hr>

## How does it work?
The APK uses a built in extractor to grab and serve data / videos, and can optionally use the [Invidious API](https://github.com/iv-org/invidious). The PWA *only* uses the Invidious API. No official YouTube APIs are used to obtain data. Your subscriptions and history are stored locally on your device and are never sent out.

## Features
* Watch videos without ads
* Use YouTube without Google tracking you using cookies and JavaScript
* Subscribe to channels without an account
* Connect to an externally setup proxy such as Tor
* View and search your local subscriptions, playlists and history
* Organize your subscriptions into "Profiles" to create a more focused feed
* Export & import subscriptions
* YouTube Trending
* YouTube Chapters
* Most popular videos page based on the set Invidious instance
* SponsorBlock 
* Full Theme support
* Keyboard shortcuts
* Option to show only family friendly content
* Show/hide functionality or elements within the app using the distraction free settings

Go to [FreeTube's Documentation](https://docs.freetubeapp.io/) if you'd like to know more about how to operate FreeTube and its features.

## Screenshots
<img alt="watching video" src="./metadata/en-US/images/phoneScreenshots/1.png" width=300> <img alt="trending videos" src="./metadata/en-US/images/phoneScreenshots/2.png" width=300> <img alt="subscription feed" src="./metadata/en-US/images/phoneScreenshots/3.png" width=300>

## How to build and test
### Commands for the APK
```bash
# 📦 Packs the project using `webpack.android.config.js`
pnpm run pack:android
# 🤖 Packs the botguard script
pnpm run pack:botGuardScript:android
# 🚧 for development
pnpm run pack:android:dev
```

> These commands only build the assets necessary for the project located in `android/` to be built. In order to obtain a complete build, you will need to build the project located in `android/` with `gradle`.
### Commands for the PWA
```bash
# 🐛 Debugs the project using `webpack.web.config.js`
pnpm run dev:web
# 📦 Packs the project using `webpack.web.config.js` 
pnpm run pack:web
```

### Commands for the PWA Docker Image
```bash
# 💨 Creates and runs the image locally. Add `--platform=linux/arm64` to docker build for ARM64 devices including Apple Silicon
docker build -t freetubeandroid2 . # Warning, might take a while on Apple Silicon
docker run --name freetubeandroid2 -d -p 8080:80 freetubeandroid2
```
## Localization
<a href="https://hosted.weblate.org/engage/free-tube/">
<img src="https://hosted.weblate.org/widgets/free-tube/-/287x66-grey.png" alt="Translation status" />
</a>

If you'd like to localize FreeTubeAndroid2, please send submissions to [FreeTube's weblate](https://hosted.weblate.org/engage/free-tube/).

## Contact
If you ever have any questions, feel free to make an issue here on GitHub. 

## Donations
If you enjoy using FreeTube Android, you're welcome to leave a donation using the following methods to support development and maintenance.  
* [Liberapay](https://liberapay.com/MarmadileManteater) _(goes to creator of FreeTubeAndroid)_
* Bitcoin Address: `1Lih7Ho5gnxb1CwPD4o59ss78pwo2T91eS` _(goes to upstream maintainers)_

While your donations are much appreciated, only donate if you really want to. Donations are no strings attached and do not come with the expectation of having your requests fulfilled.

## License
[![GNU AGPLv3 Image](https://www.gnu.org/graphics/agplv3-155x51.png)](https://www.gnu.org/licenses/agpl-3.0.html)  

FreeTube is Free Software: You can use, study share and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU Affero General Public License](https://www.gnu.org/licenses/agpl-3.0.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.  
