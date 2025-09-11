import java.util.Scanner;

public class Chat_Bot {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Hi! Welcome to Domino's. How can I help you today?");
        System.out.println("You can ask about menu, place order, or check offers.");
        
        boolean chatting = true;
        
        while (chatting) {
            System.out.print("\nYou: ");
            String userInput = sc.nextLine().toLowerCase();
            
            if (userInput.equals("bye") || userInput.equals("exit")) {
                System.out.println("Bot: Thanks for visiting Domino's! Have a great day.");
                chatting = false;
            }
            else if (userInput.contains("menu")) {
                showMenu();
            }
            else if (userInput.contains("order")) {
                takeOrder();
            }
            else if (userInput.contains("offer") || userInput.contains("discount")) {
                System.out.println("Bot: Currently we have 20% off on orders above 500 rupees. Also free delivery on all orders!");
            }
            else if (userInput.contains("hello") || userInput.contains("hi")) {
                System.out.println("Bot: Hello! Ready to order some delicious pizza?");
            }
            else if (userInput.contains("thank")) {
                System.out.println("Bot: You're welcome! Is there anything else you need?");
            }
            else {
                System.out.println("Bot: Sorry, I didn't understand. I can help with menu, orders, or offers.");
            }
        }
        
        sc.close();
    }
    
    static void showMenu() {
        System.out.println("Bot: Here's our menu:");
        System.out.println("1. Margherita - 99 rupees");
        System.out.println("2. Farmhouse - 249 rupees");
        System.out.println("3. Pepperoni - 299 rupees");
        System.out.println("4. Cheese Burst - 349 rupees");
        System.out.println("5. Veg Extravaganza - 399 rupees");
    }
    
    static void takeOrder() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bot: Which pizza would you like to order?");
        System.out.print("You: ");
        String pizzaChoice = sc.nextLine().toLowerCase();
        
        if (pizzaChoice.contains("margherita")) {
            System.out.println("Bot: Great choice! Margherita pizza added to your order. Total: 99 rupees");
        }
        else if (pizzaChoice.contains("farmhouse")) {
            System.out.println("Bot: Farmhouse pizza ordered successfully! Total: 249 rupees");
        }
        else if (pizzaChoice.contains("pepperoni")) {
            System.out.println("Bot: Pepperoni pizza confirmed! Total: 299 rupees");
        }
        else if (pizzaChoice.contains("cheese burst")) {
            System.out.println("Bot: Cheese Burst pizza selected! Total: 349 rupees");
        }
        else if (pizzaChoice.contains("veg extravaganza")) {
            System.out.println("Bot: Veg Extravaganza pizza ordered! Total: 399 rupees");
        }
        else {
            System.out.println("Bot: Sorry, I didn't understand which pizza you want. Please choose from our menu.");
        }
        
        System.out.println("Bot: Your pizza will be delivered in 30 minutes. Thank you!");
    }
}