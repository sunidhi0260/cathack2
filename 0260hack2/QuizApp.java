import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuizApp {

    private static String correctUsername = "teacher";
    private static String correctPassword = "password";
    private static int highestScore = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Login
        System.out.println("Welcome to the Quiz Application!");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (authenticate(username, password)) {
            System.out.println("Login successful! Starting the quiz...\n");
            boolean continueQuiz = true;
            while (continueQuiz) {
                int score = runQuiz(scanner);
                if (score > highestScore) {
                    highestScore = score;
                    System.out.println("Congratulations! You've achieved the highest score so far!\n");
                }
                System.out.println("Your Score: " + score);
                System.out.println("Highest Score: " + highestScore);

                System.out.print("Do you want to attempt the quiz again? (yes/no): ");
                continueQuiz = scanner.nextLine().equalsIgnoreCase("yes");
            }
        } else {
            System.out.println("Login failed! Exiting...");
        }
    }

    private static boolean authenticate(String username, String password) {
        return correctUsername.equals(username) && correctPassword.equals(password);
    }

    private static int runQuiz(Scanner scanner) {
        int score = 0;
        int totalQuestions = 0;

        try {
            File file = new File("questions.txt");
            Scanner fileScanner = new Scanner(file);
            Map<String, String> correctAnswers = new HashMap<>();

            while (fileScanner.hasNextLine()) {
                totalQuestions++;
                String question = fileScanner.nextLine();
                String answer = fileScanner.nextLine();
                String hint = fileScanner.nextLine(); // Hint line

                correctAnswers.put(question, answer);

                System.out.println("Question " + totalQuestions + ": " + question);
                System.out.print("Hint: " + hint + "\n");
                System.out.print("You have 10 seconds to answer.\nYour Answer: ");

                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < 10000 && !scanner.hasNextLine()) {
                    // Wait for user input with a timeout of 10 seconds
                }

                String userAnswer;
                if (scanner.hasNextLine()) {
                    userAnswer = scanner.nextLine();
                } else {
                    userAnswer = "";
                }

                if (userAnswer.equalsIgnoreCase(answer)) {
                    score++;
                    System.out.println("Correct!\n");
                } else {
                    System.out.println("Incorrect! The correct answer was: " + answer + "\n");
                }
            }

            // Result Analysis
            System.out.println("Quiz completed!");
            System.out.println("You scored " + score + " out of " + totalQuestions);
            System.out.println("Review:");

            for (Map.Entry<String, String> entry : correctAnswers.entrySet()) {
                System.out.println("Question: " + entry.getKey());
                System.out.println("Correct Answer: " + entry.getValue());
                System.out.println();
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: questions.txt file not found!");
        }

        return score;
    }
}