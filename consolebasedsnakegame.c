#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <windows.h>
#include <time.h>
#include <string.h>


#define WIDTH 40
#define HEIGHT 20
#define MAX_SNAKE_LENGTH 100
#define HIGHSCORE_FILE "highscore.txt"


#define KEY_UP 72
#define KEY_DOWN 80
#define KEY_LEFT 75
#define KEY_RIGHT 77
#define KEY_ESC 27


typedef struct {
    int x;
    int y;
} Position;

typedef struct {
    Position body[MAX_SNAKE_LENGTH];
    int length;
    int direction; 
} Snake;

typedef struct {
    int x;
    int y;
} Food;

typedef enum {
    EASY = 150,
    MEDIUM = 100,
    HARD = 50
} Difficulty;


int score = 0;
int highScore = 0;
int gameOver = 0;
Difficulty currentDifficulty = MEDIUM;
Snake snake;
Food food;


void hideCursor();
void gotoxy(int x, int y);
void setColor(int color);
void drawBorder();
void showMenu();
void setupGame();
void drawGame();
void input();
void logic();
void generateFood();
void saveHighScore();
void loadHighScore();
void showHighScores();
void showInstructions();
void setDifficulty();
void gameOverScreen();

int main() {
    srand(time(0));
    hideCursor();
    loadHighScore();

    while (1) {
        showMenu();
    }
    return 0;
}




void hideCursor() {
    HANDLE consoleHandle = GetStdHandle(STD_OUTPUT_HANDLE);
    CONSOLE_CURSOR_INFO info;
    info.dwSize = 100;
    info.bVisible = FALSE;
    SetConsoleCursorInfo(consoleHandle, &info);
}


void gotoxy(int x, int y) {
    COORD coord;
    coord.X = x;
    coord.Y = y;
    SetConsoleCursorPosition(GetStdHandle(STD_OUTPUT_HANDLE), coord);
}


void setColor(int color) {
    SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), color);
}



void generateFood() {
    int valid = 0;
    while (!valid) {
        food.x = (rand() % (WIDTH - 2)) + 1;
        food.y = (rand() % (HEIGHT - 2)) + 1;
        
        valid = 1;
        // Ensure food doesn't spawn on snake
        for (int i = 0; i < snake.length; i++) {
            if (snake.body[i].x == food.x && snake.body[i].y == food.y) {
                valid = 0;
                break;
            }
        }
    }
}

void setupGame() {
    gameOver = 0;
    score = 0;
    
    
    snake.length = 3;
    snake.direction = 3;
    snake.body[0].x = WIDTH / 2;
    snake.body[0].y = HEIGHT / 2;
    

    for(int i = 1; i < snake.length; i++) {
        snake.body[i].x = snake.body[0].x - i;
        snake.body[i].y = snake.body[0].y;
    }

    generateFood();
    system("cls");
    drawBorder();
}

void drawBorder() {
    setColor(9); 
    for (int i = 0; i < WIDTH; i++) {
        gotoxy(i, 0); printf("#");
        gotoxy(i, HEIGHT - 1); printf("#");
    }
    for (int i = 0; i < HEIGHT; i++) {
        gotoxy(0, i); printf("#");
        gotoxy(WIDTH - 1, i); printf("#");
    }
    setColor(15); 
}

void drawGame() {
    
    
    
    gotoxy(food.x, food.y);
    setColor(12); 
    printf("*");

    
    for (int i = 0; i < snake.length; i++) {
        gotoxy(snake.body[i].x, snake.body[i].y);
        if (i == 0) {
            setColor(10); 
            printf("O"); 
        } else {
            setColor(10); 
            printf("o");
        }
    }

    
    gotoxy(0, HEIGHT + 1);
    setColor(15);
    printf("Score: %d  |  High Score: %d", score, highScore);
}

void input() {
    if (_kbhit()) {
        int key = _getch();
        
        
        if (key == 0 || key == 224) {
            key = _getch();
            switch (key) {
                case KEY_UP:    if(snake.direction != 1) snake.direction = 0; break;
                case KEY_DOWN:  if(snake.direction != 0) snake.direction = 1; break;
                case KEY_LEFT:  if(snake.direction != 3) snake.direction = 2; break;
                case KEY_RIGHT: if(snake.direction != 2) snake.direction = 3; break;
            }
        } 
        
        else {
            switch (key) {
                case 'w': case 'W': if(snake.direction != 1) snake.direction = 0; break;
                case 's': case 'S': if(snake.direction != 0) snake.direction = 1; break;
                case 'a': case 'A': if(snake.direction != 3) snake.direction = 2; break;
                case 'd': case 'D': if(snake.direction != 2) snake.direction = 3; break;
                case 'p': case 'P': system("pause"); break; 
                case KEY_ESC: gameOver = 1; break;
            }
        }
    }
}

void logic() {
    
    Position tail = snake.body[snake.length - 1];
    
    
    for (int i = snake.length - 1; i > 0; i--) {
        snake.body[i] = snake.body[i - 1];
    }

    
    switch (snake.direction) {
        case 0: snake.body[0].y--; break;
        case 1: snake.body[0].y++; break; 
        case 2: snake.body[0].x--; break; 
        case 3: snake.body[0].x++; break; 
    }

   

    
    if (snake.body[0].x <= 0 || snake.body[0].x >= WIDTH - 1 ||
        snake.body[0].y <= 0 || snake.body[0].y >= HEIGHT - 1) {
        gameOver = 1;
        return;
    }

    
    for (int i = 1; i < snake.length; i++) {
        if (snake.body[0].x == snake.body[i].x && snake.body[0].y == snake.body[i].y) {
            gameOver = 1;
            return;
        }
    }

    
    if (snake.body[0].x == food.x && snake.body[0].y == food.y) {
        score += 10;
        if (score > highScore) highScore = score;
        

        if (snake.length < MAX_SNAKE_LENGTH) {
            snake.length++;
            
            snake.body[snake.length-1] = tail; 
        }
        generateFood();
    } else {
        
        gotoxy(tail.x, tail.y);
        printf(" ");
    }
}



void showMenu() {
    system("cls");
    setColor(14); 
    printf("\n  CONSOLE SNAKE GAME v2.0 (C)\n");
    printf("  ===========================\n\n");
    setColor(15); 
    printf("  1. Start New Game\n");
    printf("  2. View High Scores\n");
    printf("  3. Difficulty Settings\n");
    printf("  4. Instructions\n");
    printf("  5. Exit\n\n");
    printf("  Enter your choice: ");

    char choice = _getch();
    switch (choice) {
        case '1':
            setupGame();
            while (!gameOver) {
                drawGame();
                input();
                logic();
                Sleep(currentDifficulty); 
            }
            gameOverScreen();
            break;
        case '2': showHighScores(); break;
        case '3': setDifficulty(); break;
        case '4': showInstructions(); break;
        case '5': exit(0);
        default: break;
    }
}

void showHighScores() {
    system("cls");
    setColor(11);
    printf("\n  HIGH SCORES BOARD\n");
    printf("  =================\n\n");
    setColor(15);
    printf("  Current High Score: %d\n\n", highScore);
    printf("  (Saved in %s)\n\n", HIGHSCORE_FILE);
    printf("  Press any key to return to menu...");
    _getch();
}

void setDifficulty() {
    system("cls");
    setColor(11);
    printf("\n  SELECT DIFFICULTY LEVEL\n");
    printf("  =======================\n\n");
    setColor(15);
    printf("  1. Easy   (Slow)\n");
    printf("  2. Medium (Normal)\n");
    printf("  3. Hard   (Fast)\n\n");
    printf("  Current setting: ");
    if(currentDifficulty == EASY) printf("EASY\n");
    else if(currentDifficulty == MEDIUM) printf("MEDIUM\n");
    else printf("HARD\n");
    
    printf("\n  Press number to select: ");
    
    char choice = _getch();
    switch(choice) {
        case '1': currentDifficulty = EASY; break;
        case '2': currentDifficulty = MEDIUM; break;
        case '3': currentDifficulty = HARD; break;
    }
}

void showInstructions() {
    system("cls");
    setColor(14);
    printf("\n  INSTRUCTIONS\n");
    printf("  ============\n\n");
    setColor(15);
    printf("  - Use WASD or Arrow Keys to move.\n");
    printf("  - Eat Food (*) to grow and earn points.\n");
    printf("  - Avoid hitting Walls (#) or yourself.\n");
    printf("  - Press 'P' to Pause.\n");
    printf("  - Press 'ESC' to Quit during game.\n\n");
    printf("  Press any key to return...");
    _getch();
}

void gameOverScreen() {
    saveHighScore();
    system("cls");
    setColor(12); 
    printf("\n  GAME OVER!\n");
    printf("  ==========\n\n");
    setColor(15);
    printf("  Final Score: %d\n", score);
    printf("  High Score:  %d\n\n", highScore);
    
    printf("  Press 'R' to Restart or 'M' for Main Menu\n");
    
    while(1) {
        char choice = _getch();
        if (choice == 'r' || choice == 'R') {
            setupGame();
            while (!gameOver) {
                drawGame();
                input();
                logic();
                Sleep(currentDifficulty);
            }
            gameOverScreen();
            return; 
        }
        if (choice == 'm' || choice == 'M') return;
    }
}



void saveHighScore() {
    FILE *fp = fopen(HIGHSCORE_FILE, "w");
    if (fp != NULL) {
        fprintf(fp, "%d", highScore);
        fclose(fp);
    }
}

void loadHighScore() {
    FILE *fp = fopen(HIGHSCORE_FILE, "r");
    if (fp != NULL) {
        fscanf(fp, "%d", &highScore);
        fclose(fp);
    } else {
        highScore = 0;
    }
}