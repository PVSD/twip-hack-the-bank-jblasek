package com.company;

import java.io.*;
import java.util.*;
import java.text.*;



public class Main {
    static class compare implements Comparator<bankAccount>
    {
        public int compare(bankAccount bA1, bankAccount bA2)
        {
            return (bA1.balance < bA2.balance) ? 1 : -1;
        }

    }

    static class compareDoubles implements Comparator<Double>
    {
        public int compare(Double d1, Double d2)
        {
            return (d1 < d2) ? 1 : -1;
        }
    }
    public static void main(String[] args) throws IOException{
	// write your code here

        bankAccount mrP = new bankAccount("Mr. Pennebacker", 100000);
        //Makes a txt file
        File bAFile = new File("BankAccounts.txt");
        bAFile.createNewFile();

        //make a file writer to write to the file
        FileWriter bAFileWriter = new FileWriter(bAFile);

        //make a new arrayList of all bank account objects
        ArrayList<bankAccount> bAArrayList = new ArrayList();

        //arrayList for deposits
        ArrayList<Double> bAArrayListDeposit = new ArrayList();

        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);
        String name, s;
        ArrayList aryLst = new ArrayList();
        ListIterator iter = aryLst.listIterator();

        do {

            Scanner kbReader = new Scanner(System.in);
            System.out.print("Please enter the name to whom the account belongs. (\"Exit\" to abort)");
            name = kbReader.nextLine();

            if(name.equalsIgnoreCase("debug")){
                while(true) {
                    System.out.println("Do you want to check the list of 'deposits', the list of 'balances', or do you want to 'drain' another account into Mr. P's bank account? ('EXIT' to leave the debug menu)");
                    String sDebug = kbReader.next();

                    if(sDebug.equalsIgnoreCase("deposits")){
                        Collections.sort(bAArrayListDeposit, new compareDoubles());

                        for (int i = 0; i < bAArrayListDeposit.size(); i++) {
                            System.out.print(bAArrayListDeposit.get(i) + ", ");
                        }

                        System.out.println(" ");
                    }

                    else if(sDebug.equalsIgnoreCase("balances")){
                        Collections.sort(bAArrayList, new compare());
                        for (int i = 0; i < bAArrayList.size(); i++) {
                            System.out.print(bAArrayList.get(i).balance + ", ");
                        }
                        System.out.println(" ");
                    }

                    else if(sDebug.equalsIgnoreCase("drain")){
                        System.out.println("What account do you want to drain into Mr. p");
                        sDebug = kbReader.next();

                        for (int i = 0; i < bAArrayList.size(); i++) {
                            if(sDebug.equalsIgnoreCase(bAArrayList.get(i).name)){
                                double money = bAArrayList.get(i).balance;
                                bAArrayList.get(i).balance = 0;
                                System.out.println(bAArrayList.get(i).name + " lost " + money + ". They now have $" + fmt.format(bAArrayList.get(i).balance) + ".");

                                mrP.balance += money;
                                System.out.println("Mr. p now has all of the money. He now has " + fmt.format(mrP.balance) + ".");
                            }
                        }
                    }
                    else if(sDebug.equalsIgnoreCase("exit")){
                        System.out.println("ok cool" + "\n");
                        break;
                    }

                }
            }

            else if (!name.equalsIgnoreCase("EXIT")) {
                //puts the name into the file
                bAFileWriter.write(bankAccount.getTime() + " " + name);

                System.out.print("Please enter the amount of the deposit. ");
                double amount = kbReader.nextLong();

                bAArrayListDeposit.add(amount);
                //puts the amount into the file
                bAFileWriter.write(" " + Double.toString(amount));

                System.out.println(" "); // gives an eye pleasing blank line
                // between accounts
                bankAccount theAccount = new bankAccount(name, amount);
                bAArrayList.add(theAccount);
                iter.add(theAccount);
                //skips to the next line
                bAFileWriter.write("\n");

                //withdraw and deposit stuff
                while(true) {
                    System.out.println("Do you want to withdraw or deposit money into any account? ('deposit', 'withdraw', or 'no')");
                    s = kbReader.next();

                    if (s.equalsIgnoreCase("withdraw")) {
                        System.out.println("What account do you want to withdraw money from?");
                        s = kbReader.next();

                        for(int i = 0; i < bAArrayList.size(); i++) {

                            if ( s.equals(bAArrayList.get(i).name) ){
                                System.out.println("What the the amount you want to withdraw");
                                int amt = kbReader.nextInt();

                                bAArrayList.get(i).withdraw(amt);
                                bAFileWriter.write(" " + bankAccount.getTime() + " " + bAArrayList.get(i).name + " withdrew " + amt + " dollars");
                                System.out.println(bAArrayList.get(i).name + " withdrew " + amt + " dollars");
                            }

                        }
                        System.out.println(" ");
                        break;
                    }

                    if (s.equalsIgnoreCase("deposit")) {
                        System.out.println("What account do you want to deposit money into?");
                        s = kbReader.next();

                        for(int i = 0; i < bAArrayList.size(); i++) {

                            if ( s.equals(bAArrayList.get(i).name) ){
                                System.out.println("What the the amount you want to deposit");
                                Double amt = kbReader.nextDouble();

                                bAArrayList.get(i).deposit(amt);
                                bAArrayListDeposit.add(amt);
                                bAFileWriter.write( bankAccount.getTime() + " " + bAArrayList.get(i).name + " deposit " + amt + " dollars");
                                System.out.println(bAArrayList.get(i).name + " deposit " + amt + " dollars");
                            }


                        }
                        System.out.println(" ");
                        break;
                    }
                    if (s.equalsIgnoreCase("no")) {
                        System.out.println("Ok cool" + "\n");
                        break;
                    }
                    else {
                        System.out.println("Sorry i did not get that, try again");
                    }
                }
            }



        } while (!name.equalsIgnoreCase("EXIT"));
        bAFileWriter.close();
        // Search aryLst and print out the name and amount of the largest bank
        // account
        bankAccount ba = (bankAccount) iter.previous();
        double maxBalance = ba.balance; // set last account as the winner so far
        String maxName = ba.name;
        while (iter.hasPrevious()) {
            ba = (bankAccount) iter.previous();
            if (ba.balance > maxBalance) {
                // We have a new winner, chicken dinner
                maxBalance = ba.balance;
                maxName = ba.name;
            }
        }
        System.out.println(" ");
        System.out.println("The account with the largest balance belongs to "
                + maxName + ".");
        System.out.println("The amount is $" + fmt.format(maxBalance) + ".");

    }
}
