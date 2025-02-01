# NFC-based Event Check-In System

This Android application uses NFC technology to create a simple and efficient event check-in system. Attendees use their NFC-enabled smartphones to tap an NFC tag which logs their attendance. 
This system is designed for small to medium-sized events such as workshops, seminars and gatherings, providing a low-cost alternative to traditional attendance tracking methods.

## Features:
- **Simple NFC Check-In:** Attendees use their NFC-enabled smartphones to tap an NFC tag, which triggers the app to log their attendance. The NFC tag's serial number is used to verify each check-in.
- **Efficient Local Logging:** The app logs check-in data, including the time and attendee information and stores it locally on the device in a Room database for easy access by event organisers.
- **Customisable NFC Tag:** The system works with a specific NFC tag but the serial number is hardcoded and can be modified to accommodate different tags for various events.
- **Cost-Effective:** The app provides a simple, low-cost solution for event attendance tracking, requiring only NFC-enabled smartphones and 1 tag per event - no additional hardware needed.

## Screenshots:
Coming soon

## Tech stack:
- Kotlin
- Jetpack Compose
- Material Design 3
- Room Database 
- NFC Technology 
- ExoPlayer
- Google Authentication

## Requirements:
- Android Device with NFC Support
- 1 NFC Tag 

## Installation:
1. Clone the repository.
2. Build the project using Android Studio.
3. Deploy to an NFC-enabled Android device.
4. Ensure you have at least one NFC tag with the correct serial number, or update the serial number in the code's [MockEventData.kt](app/src/main/java/com/cbf/nfceventcheckin/MockEventData.kt) to match your serial number.
5. Your NFC tag will need to have a random plain/text record added to it to be recognised by this app. You can use an existing app like NFC Tools to write to it.

## Signing In
- There are two ways to sign in: using your Gmail account or any email address.
- As the focus of this project is not on email security or verification, no validation is performed. You can enter any value in the email field to proceed.
- To log in as an admin user and explore the admin flow, use the hardcoded email address: `admin@email.com`.
- You can also log in using a Gmail account. Note that the Firebase project information is publicly available in this repository because this is a test project. The Firebase account has several restrictions in place, limiting its use to this project only.
- If you wish to use this project for your own purposes, you will need to create your own Firebase project and generate a `google-services.json` file for it.

## Testing:
Currently, the NFC functionality **cannot be tested on an emulator** due to the lack of NFC support in most emulator environments. All testing must be done manually on an actual Android device with NFC capabilities. Simply tap the NFC tag with your phone to verify the check-in functionality.

## Future Improvements:
- **Centralised Data Storage:** Currently, data is stored locally on the user's device. In future versions, the app will be enhanced to upload check-in data to a central server via an API, allowing for real-time access and better data management by event organisers

## Contributing:
This is a **test project** and is primarily focused on experimenting with **NFC technology** and how it can be applied to event check-ins. Therefore, the emphasis is more on the NFC implementation rather than adhering to ideal software architecture or organisational patterns.
While the code is functional, it may not follow best practices for large-scale production applications. 

**Contributions, suggestions, and feedback** regarding the NFC implementation are welcome, especially from those with experience in NFC technology or event management applications.

