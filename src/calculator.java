import java.util.*;
public class calculator {
    public static void main(String[]args){
        double result,a,b;
        Scanner sc =new Scanner(System.in);
        System.out.println("1.Addition");
        System.out.println("2.Subtraction");
        System.out.println("3.Multiplication");
        System.out.println("4.Division");
        System.out.println("5.Modulus");
        System.out.println("6.exponent");
        System.out.println("7.exit");
        System.out.print("ENTER YOUR CHOICE:");
        int choice=sc.nextInt();

        if(choice==7){
            System.out.println("Intresting...thank you for your time");
            return;
        }
        System.out.print("Enter 1st value:");
         a=sc.nextDouble();

        System.out.print("Enter 2nd value:");
         b=sc.nextDouble();

        switch(choice){
            case 1:
                result=a+b;
                System.out.println("Result="+result);
                break;
            case 2:
                result=a-b;
                System.out.println("Result="+result);
                break;
            case 3:
                result=a*b;
                System.out.println("Result="+result);
                break;
            case 4:
                result=a/b;
                System.out.println("Result="+result);
                break;
            case 5:
                result=a%b;
                System.out.println("Result="+result);
                break;
            case 6:
                result=Math.pow(a,b);
                System.out.println("Result="+result);
                break;
            default:
                System.out.println("Nigga enter a valid choice :( ");
        }
        System.out.println("Thank you for ur time");
    }
}
