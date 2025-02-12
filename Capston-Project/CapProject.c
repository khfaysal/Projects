#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <conio.h>
#include <windows.h>

#define MU 50
#define ML 40
#define ROOMS 10

typedef struct
{
    char username[ML];
    char password[ML];
} User;

User userDatabase[MU];
int userCount = 0;
char name[ML];

char roomBookings[ROOMS][ML] = {""};

bool registerUser()
{
    char username[ML], password[ML];
    printf("\nEnter a username: ");
    scanf("%s", username);

    for (int i = 0; i < userCount; i++)
    {
        if (strcmp(userDatabase[i].username, username) == 0)
        {
            printf("Username already exists. Please try a different one.\n");
            return false;
        }
    }

    printf("Enter a password: ");

    int index = 0;
    char ch;
    while (1)
    {
        ch = getch();
        if (ch == '\r')
        {
            password[index] = '\0';
            break;
        }
        else if (ch == '\b' && index > 0)
        {
            index--;
            printf("\b \b");
        }
        else if (ch != '\b')
        {
            if (index < ML - 1)
            {
                password[index++] = ch;
                printf("*");
            }
        }
    }
    printf("\n");

    FILE *file = fopen("user.csv", "a");
    if (file == NULL)
    {
        perror("Unable to open user.csv");
        return false;
    }

    fprintf(file, "%s,%s\n", username, password);
    fclose(file);

    strcpy(userDatabase[userCount].username, username);
    strcpy(userDatabase[userCount].password, password);
    userCount++;

    printf("Registration successful!\n");
    return true;
}

bool loginUser(char *loggedInUser)
{
    char username[ML], password[ML];
    printf("\nEnter your username: ");
    scanf("%s", username);
    printf("Enter your password: ");

    int index = 0;
    char ch;
    while (1)
    {
        ch = getch();
        if (ch == '\r')
        {
            password[index] = '\0';
            break;
        }
        else if (ch == '\b' && index > 0)
        {
            index--;
            printf("\b \b");
        }
        else if (ch != '\b')
        {
            if (index < ML - 1)
            {
                password[index++] = ch;
                printf("*");
            }
        }
    }
    printf("\n");

    strcpy(name, username);

    for (int i = 0; i < userCount; i++)
    {
        if (strcmp(userDatabase[i].username, username) == 0 &&
            strcmp(userDatabase[i].password, password) == 0)
        {
            strcpy(loggedInUser, username);
            printf("Login successful. Welcome to RMS..., %s!\n", username);
            return true;
        }
    }

    printf("Invalid username or password..\n");
    return false;
}

void guestAccess()
{

    printf("\nWelcome, Guest! You have limited access to the RMS system.\n");
    printf("Experience cozy and comfortable rooms at incredibly affordable rates.\n");
    printf("Your comfort is our priority, and we ensure a delightful stay for everyone.\n");
    printf("Interested?\n");
    printf("Visit our website, register today, and start enjoying the experience.\n");
    printf("Wishing you a wonderful day ahead!\n");
}

void displayRooms()
{
    FILE *file = fopen("bookedrooms.csv", "r");

    if (file == NULL)
    {
        perror("Unable to open file");
        return;
    }

    int roomNumber;
    char loggedInUser[50];
    int booked;
    printf("\033[31m");
    printf("=====================================\n");
    printf("             Room Status             \n");
    printf("=====================================\n");
    printf("\033[36m");
    for (int i = 0; i < ROOMS; i++)
    {
        booked = 0;

        rewind(file);

        while (fscanf(file, "%d,%49[^\n]", &roomNumber, loggedInUser) != EOF)
        {
            if (roomNumber == i + 1)
            {
                printf("Room %d is booked by %s\n", roomNumber, loggedInUser);
                booked = 1;
                break;
            }
        }

        if (booked == 0)
        {
            printf("Room %d is available\n", i + 1);
        }
    }

    fclose(file);
}

void processPayment()
{
    int paymentChoice;
    printf("\033[1;31m");
    printf("*****************************\n");
    printf("*   Select Payment Method   *\n");
    printf("*****************************\n");
    printf("\033[36m");
    printf("1. Bkash\n");
    printf("2. Nagad\n");
    printf("3. Cash\n");
    printf("4. Bank Transfer\n");
    printf("Enter your choice: ");
    scanf("%d", &paymentChoice);

    switch (paymentChoice)
    {
    case 1:
        printf("Payment through Bkash has been successfully completed.\n");
        Beep(300, 150);
        break;
    case 2:
        printf("Payment through Nagad has been successfully completed.\n");
        Beep(300, 150);
        break;
    case 3:
        printf("Cash payment has been confirmed.\n");
        Beep(300, 150);
        break;
    case 4:
        printf("Payment through Bank Transfer has been successfully completed.\n");
        Beep(300, 150);
        break;
    default:
        printf("Invalid payment method. Please try again.\n");
        processPayment(); // Recursive call for invalid input
    }
}

void bookRoom(const char *loggedInUser)
{
    int roomNumber;
    printf("\033[1;35m");
    printf("\nEnter the room number you want to book (1-10): ");
    printf("\033[36m");
    scanf("%d", &roomNumber);

    if (roomNumber < 1 || roomNumber > ROOMS)
    {
        printf("Invalid room number. Please select a room between 1 and 10.\n");
        return;
    }

    if (strlen(roomBookings[roomNumber - 1]) > 0)
    {
        printf("Room %d is already booked by %s. Please choose a different room.\n", roomNumber, roomBookings[roomNumber - 1]);
    }
    else
    {
        strcpy(roomBookings[roomNumber - 1], loggedInUser);
        printf("\nRoom %d has been successfully booked by %s.\n", roomNumber, loggedInUser);
        processPayment();

        FILE *file = fopen("bookedrooms.csv", "a");
        if (file == NULL)
        {
            perror("Unable to open file");
            return;
        }

        fprintf(file, "%d,%s\n", roomNumber, loggedInUser);

        fclose(file);
    }
}

void manageBookings(const char *loggedInUser)
{
    int choice;
    while (true)
    {
        printf("\033[1;35m");
        printf("=====================================\n");
        printf("        Room Management Menu:        \n");
        printf("=====================================\n");
        printf("\033[36m");
        printf("1. Display Available Rooms\n");
        printf("2. Book a Room\n");
        printf("3. Logout\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice)
        {
        case 1:
            displayRooms();
            break;
        case 2:
            bookRoom(loggedInUser);
            break;
        case 3:
            printf("Logging out...\n");
            return;
        default:
            printf("Invalid choice. Please select a valid option (1-3).\n");
        }
    }
}

int main()
{
    char loggedInUser[ML] = ""; // Stores the name of the currently logged-in user
    while (true)
    {
        int choice;
        printf("\n\n\n\n");
        printf("\033[32m");
        printf("#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#\n");
        printf(" |                                           |\n");
        printf("|->   Welcome to Rental Management System   <-|\n");
        printf(" |                                           |\n");
        printf("#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#-#_#\n\n");
        printf("\033[36m");
        printf("                 1. Register\n");
        printf("                  2. Login\n");
        printf("             3. Continue as Guest\n");
        printf("\033[31m");
        printf("                   4. Exit\n\n");
        printf("\033[36m");
        printf("           Enter your choice Please: ");
        scanf("%d", &choice);

        switch (choice)
        {
        case 1:
            registerUser();
            break;
        case 2:
            if (loginUser(loggedInUser))
                manageBookings(loggedInUser);
            break;
        case 3:
            guestAccess();
            return 0;
        case 4:
            printf("Exiting the program...\n");
            printf("The Program Exit Successfully.");
            return 0;
        default:
            printf("Invalid choice. Please select a valid option (1-4).\n");
        }
    }
}
