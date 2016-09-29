## rn-youtube-android [![npm version](https://badge.fury.io/js/rn-youtube-android.svg)](https://badge.fury.io/js/rn-youtube-android)

> **Important: Android only**

Play videos from YouTube using YouTube's API, in React Native.

## Known Issues
:danger: Works reliably only up to version 0.30. :danger:
Between versions 0.31-0.33 of react-native there's a breaking change in the activity listener/result handling which can crash the application. Work in under way, thanks for your patience.

## Notice
- Since it is a React Native project, it was developed and tested on OSX only, I apologize for the linux folks.
- Intended **only** for existing Android apps that are integrating React Native - [Facebook's instructions]( https://facebook.github.io/react-native/docs/embedded-app-android.html).
- Make sure you have the latest React Native/Babel/random cool stuff in your project.
- Using `YouTubeAndroidPlayerApi-1.2.2.zip` - [current version](https://developers.google.com/youtube/android/player/downloads/)

## Getting Started
- Add to `settings.gradle`:
```
include ':RNYouTubePlayer', ':app'
project(':RNYouTubePlayer').projectDir = new File(rootProject.projectDir, '<your node_modules path>/rn-youtube-android/android')
```
- Add to `build.gradle`:
```
dependencies {
    ...
    compile project(':RNYouTubePlayer')
}
```
- In the activity that starts React Native, add the package to the `ReactInstanceManager` builder: 
```
import com.applicaster.RNYouTubePlayer.YoutubePlayerReactPackage;

...

Builder reactManagerBuilder = ReactInstanceManager.builder()
    .setApplication(getApplication())
    .addPackage(new MainReactPackage())
    .addPackage(new YoutubePlayerReactPackage())
    ...
```

- **Important**: In order to get callbacks from the YouToube player, which runs on a separate activity, add this to the activity that holds the `ReactInstanceManager`:
```
private ReactInstanceManager reactInstanceManager; // Initialize it following React Native instructions, above

...

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    reactInstanceManager.onActivityResult(requestCode, resultCode, data);
}
```

## Usage
- Set up your `<YouTube api token>`: [Google is your friend](https://developers.google.com/youtube/registering_an_application#create_project)
- Choose a video from YouTube, like this shameless plug: `KawcajJGX-w`
- Add to your `index.android.js` or anywhere inside the project:
```
import RNYouTubePlayer from 'rn-youtube-android';
RNYouTubePlayer.play(<youtube api token>, <youtube video url - hash only>);
```
- It is highly recommended to surround the player with `await / async` in order to reap the future benefits of callbacks:
```
playVideo = async function(props, callback) { // example: token and video url exists in props
  try {
    let {
      state,
    } = await RNYouTubePlayer.play(props.apiToken, props.videoURL);

    if (state === 'stopped') {
      callback(); // stopped watching video, do something
    }
  } catch (e) {
    console.error(e);
  }
};
```


---
`YouTubeAndroidPlayerApi.jar` belongs to Google, Inc. - no copyright infringement intended. 
