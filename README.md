# ✅ To Today Alarm - A To-Do List with Reminders

* **Discipline:** Programming for Mobile Devices
* **Teacher:** Junio Moreira
* **Student:** João Augusto Marciano Silva
* **Final date:** 14/12/2025

## Application operation

### Dark mode

| List Screen | Add Task Modal | Add Date | Add Time | Notification |
|:---:|:---:|:---:|:---:|:---:|
| <img height="500" alt="List Screen Dark" src="https://github.com/user-attachments/assets/894cbfd1-4c87-4157-b893-034ac21a9b80" /> | <img height="500" alt="Add Task Modal Dark" src="https://github.com/user-attachments/assets/2e52d71c-48ce-47c0-aab9-f1ca57040e4e" /> | <img height="500" alt="Add Date Dark" src="https://github.com/user-attachments/assets/059c29b7-b959-4044-a81c-febe8988f39a" /> | <img height="500" alt="Add Time Dark" src="https://github.com/user-attachments/assets/4ea44c03-39ad-423e-ae6a-996790c86752" /> | <img height="500" alt="Notification Dark"  src="https://github.com/user-attachments/assets/3b57652a-e3cf-49d3-9eb4-1a7d5c4a57e4" /> |

### Light mode

| List Screen | Add Task Modal | Add Date | Add Time | Notification |
|:---:|:---:|:---:|:---:|:---:|
| <img height="500" alt="List Screen Light" src="https://github.com/user-attachments/assets/432ccc2c-b93b-401a-95c3-609f3e234681" /> | <img height="500" alt="Add Task Modal Light" src="https://github.com/user-attachments/assets/c7d124ca-bd72-4a62-a1cc-e3b595631297" /> | <img height="500" alt="Add Date Light" src="https://github.com/user-attachments/assets/3dc46f47-afc3-45d6-a50c-3f07ee9a3c03" /> | <img height="500" alt="Add Time Light" src="https://github.com/user-attachments/assets/fe19eab9-e633-4bf6-bd83-40f2f442cc27" /> | <img height="500" alt="Notification Light" src="https://github.com/user-attachments/assets/21dee315-60f3-41cb-9415-ef0ca1908250" /> |

## Features

* **Task Management:** Allows users to add, edit, and delete tasks seamlessly.
* **Smart Reminders (New):**
    - **Scheduled Notifications:** Users can set specific dates and times for tasks using Material 3 Date and Time pickers.
    - **Local Alarms:** Utilizes `AlarmManager` and `BroadcastReceiver` to trigger precise notifications even when the application is closed or the device is in Doze mode.
    - **Visual Indicators:** Tasks with scheduled reminders display a clock icon and the formatted date/time directly in the list.
* **Permissions Handling:** Fully compatible with Android 13+ (API 33) runtime notification permissions.
* **Status Toggling:** Mark tasks as "Completed" or "Pending" with a simple checkbox.
* **Instant Search:** A persistent search bar filters the task list in real-time by matching task titles.
* **Dynamic Sorting:** Users can instantly re-organize the list using four criteria:
    - Order A-Z
    - Order Z-A
    - Completed First
    - Pending First (Default)
* **Modal Input:** A clean "Add Task" modal (`AlertDialog`) provides a focused data entry experience for new tasks.
* **Edit Screen:** Clicking a task navigates to a separate "Edit" screen where the title, description, and **reminder time** can be modified.
* **Modern UI:** Built entirely with Jetpack Compose and Material Design 3, using components like `Scaffold`, `LazyColumn`, `Card`, and `OutlinedTextField`.
* **Custom Theming:** Features a vibrant, custom yellow-based theme with full support for system Light and Dark modes.
* **UX Enhancements:**
    - The "Title" field automatically gains focus when the "Add Task" modal is opened.
    - The on-screen keyboard automatically enables sentence capitalization (`KeyboardCapitalization.Sentences`) for new tasks.
* **State Management:** Uses an `AndroidViewModel` to manage application state and database logic, ensuring data persists across configuration changes.
* **Fluid Navigation:** Implements Jetpack Navigation, with custom `EnterTransition.None` and `ExitTransition.None` to remove fade-in/out animations for faster, more direct screen changes.

## Download

[APK](https://github.com/joaomarcianodev/DM-Prova5/blob/main/app/release/ToToday-1.0.apk)
