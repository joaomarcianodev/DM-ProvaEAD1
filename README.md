# ✅ To Today - A To-Do List

- **Discipline:** Programming for Mobile Devices
- **Teacher:** Junio Moreira
- **Student:** João Augusto Marciano Silva
- **Final date:** 18/11/2025

## Application operation

### Dark mode

| List Screen (All) | List Screen (Searching) |  Add Task Modal | Edit Task Screen | Order List |
|:---:|:---:|:---:|:---:|:---:|
| <img height="500" alt="Searching List Screen Dark" src="https://github.com/user-attachments/assets/d91530ff-f034-487e-a49b-86e36b26c777" /> | <img height="500" alt="All List Screen Dark" src="https://github.com/user-attachments/assets/8290bf5e-ac45-4ae3-a16b-ca9395159e00" /> | <img height="500" alt="Add Task Screen Dark" src="https://github.com/user-attachments/assets/5f83bb52-3b73-48b8-9820-109c011e1662" /> | <img height="500" alt="Edit Screen Dark" src="https://github.com/user-attachments/assets/065faf35-f2d6-48e4-adab-f08a0bf9a8c9" /> | <img height="500" alt="Order Screen Dark" src="https://github.com/user-attachments/assets/f4746b4d-1fc6-4775-a809-defd472cf528" /> |

## Features

- **Task Management:** Allows users to add, edit, and delete tasks seamlessly.
- **Status Toggling:** Mark tasks as "Completed" or "Pending" with a simple checkbox.
- **Instant Search:** A persistent search bar filters the task list in real-time by matching task titles.
- **Dynamic Sorting:** Users can instantly re-organize the list using four criteria:
    - Order A-Z
    - Order Z-A
    - Completed First
    - Pending First (Default)
- **Modal Input:** A clean "Add Task" modal (`AlertDialog`) provides a focused data entry experience for new tasks.
- **Edit Screen:** Clicking a task navigates to a separate "Edit" screen where both the title and description can be modified.
- **Modern UI:** Built entirely with Jetpack Compose and Material Design 3, using components like `Scaffold`, `LazyColumn`, `Card`, and `OutlinedTextField`.
- **Custom Theming:** Features a vibrant, custom yellow-based theme with full support for system Light and Dark modes.
- **UX Enhancements:**
    - The "Title" field automatically gains focus when the "Add Task" modal is opened.
    - The on-screen keyboard automatically enables sentence capitalization (`KeyboardCapitalization.Sentences`) for new tasks.
- **State Management:** Uses a `ViewModel` to manage application state, ensuring data persists across configuration changes (like rotation) and is shared between screens.
- **Fluid Navigation:** Implements Jetpack Navigation, with custom `EnterTransition.None` and `ExitTransition.None` to remove fade-in/out animations for faster, more direct screen changes.

## Download

[APK](https://github.com/joaomarcianodev/DM-Prova5/blob/main/app/release/ToToday-1.0.apk)
