import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Project2 {

    public static int numberOfVariablesInInput;
    public static int numberOfVariablesInStack;
    public static int numberOfGoalStates;
    public static int numberOfStates;
    public static List<State> states;
    public static StartState startState;
    public static List<FinalState> goalStates;
    public static List<String> stackAlphabet;
    public static String initialStackSymbol;
    public static List<String> inputAlphabet;
    public static List<String> transitionFunction;
    public static Matrix transitionMatrix;
    public static List<String> stringsToBeDetected;

    public static void main(String[] args) {

        //This part takes all the data in the file with the format needed.

        String fileName = "input_file.txt";
        List<String> data = new ArrayList<String>();

        try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                //data contains all characters in the file
                data.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("output_file.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



        numberOfVariablesInInput = Integer.parseInt(data.get(0).substring(0, 1))+1;

        numberOfVariablesInStack = Integer.parseInt(data.get(1).substring(0, 1))+1;

        numberOfGoalStates = Integer.parseInt(data.get(2).substring(0, 1));

        numberOfStates = Integer.parseInt(data.get(3).substring(0, 1));

        states = new ArrayList<State>();
        for (String state : data.get(4).split("\\s+")) {
            states.add(new State(state));
        }

        startState = new StartState(data.get(5));

        goalStates = new ArrayList<FinalState>();
        for (String state : data.get(6).split("\\s+")) {
            goalStates.add(new FinalState(state));
        }

        stackAlphabet = new ArrayList<String>();
        for (String symbol : data.get(7).split("\\s+")) {
            stackAlphabet.add(symbol);
        }
        stackAlphabet.add("ε");

        initialStackSymbol = data.get(8);

        inputAlphabet = new ArrayList<String>();
        for (String symbol : data.get(9).split("\\s+")) {
            inputAlphabet.add(symbol);
        }
        inputAlphabet.add("ε");



        transitionFunction = new ArrayList<>();
        int index = 10;
        while (data.get(index).contains("q")){
            for (String element : data.get(index).split("\\s+")) {
                transitionFunction.add(element);
            }
            index++;
        }


        transitionMatrix = new Matrix(numberOfStates, numberOfVariablesInInput*numberOfVariablesInStack);


        for(int i = 0; i<index-10; i++){

            State fromState = new State(transitionFunction.get(i*5+0));
            String inputSymbol = transitionFunction.get(i*5+1);
            String popSymbol = transitionFunction.get(i*5+2);
            String pushSymbol = transitionFunction.get(i*5+3);
            State toState = new State(transitionFunction.get(i*5+4));

            /*
            System.out.println(fromState);
            System.out.println(inputSymbol);
            System.out.println(popSymbol);
            System.out.println(pushSymbol);
            System.out.println(toState);
             */

            int inputSymbolIndex = 0;
            int k = 0;
            for (String inputElement : inputAlphabet){
                if (inputSymbol.equals(inputElement)){
                    inputSymbolIndex=k;
                }
                k++;
            }

            k = 0;
            int popSymbolIndex = 0;
            for (String popElement : stackAlphabet){
                if (popSymbol.equals(popElement)){
                    popSymbolIndex=k;
                }
                k++;
            }

            int column = inputSymbolIndex*(numberOfVariablesInStack)+popSymbolIndex;

            transitionMatrix.setMatrixValues(fromState.getNumber()-1, column, toState, pushSymbol);
            /*
            System.out.println(transitionMatrix.getMatrixValues(fromState.getNumber()-1, column));
            for (String symbol : transitionMatrix.getMatrixValues(fromState.getNumber()-1, column).split("\\s+")) {
                System.out.println(symbol);
            }
            */
        }

        System.out.println(transitionMatrix);

        stringsToBeDetected = new ArrayList<String>();
        for (int i = index; i < data.size(); i++) {
            stringsToBeDetected.add(data.get(i));
        }

        /*
        System.out.println(numberOfVariablesInInput);
        System.out.println(numberOfVariablesInStack);
        System.out.println(numberOfGoalStates);
        System.out.println(numberOfStates);
        System.out.println(states);
        System.out.println(startState);
        System.out.println(goalStates);
        System.out.println(stackAlphabet);
        System.out.println(initialStackSymbol);
        System.out.println(inputAlphabet);
        System.out.println(transitionFunction);
        System.out.println(stringsToBeDetected);
         */

        //This part simulates the behaviour of PDA.
        List<List<State>> routeTakens = new ArrayList<>();
        for(String stringToBeDetected : stringsToBeDetected){
            Stack stack = new Stack();
            List<State> routeTaken = new ArrayList<>();
            routeTaken.add(new State(startState.getName()));
            routeTakens.add(PDA(stringToBeDetected, routeTaken, stack));
        }

        String output = "";
        for (List<State> route : routeTakens){
            String isAccepted = "Rejected!";
            if(!route.get(route.size() - 1).getName().equals("d1")) {
                for (FinalState finalState : goalStates) {
                    if (finalState.getName().equals(route.get(route.size() - 1).getName())) {
                        isAccepted = "Accepted!";
                    }
                }
            }else{
                route.remove(route.size() - 1);
            }
            output += route + "\n";
            output += isAccepted + "\n";
        }


        try {
            FileWriter myWriter = new FileWriter("output_file.txt");
            myWriter.write(output);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static List<State> PDA(String stringToBeDetected, List<State> routeTaken, Stack stack){
        if(stringToBeDetected.length()>1){
            String input = stringToBeDetected.substring(0, 1);
            stringToBeDetected = stringToBeDetected.substring(1);
            int inputIndex=0;
            int f=0;
            for (String symbol: inputAlphabet){
                if (symbol.equals(input)){
                    inputIndex= f;
                }
                f++;
            }
            State currentState = routeTaken.get(routeTaken.size()-1);
            String pop = stack.popControl();
            int popIndex = 0;
            f=0;
            for (String symbol: stackAlphabet){
                if (symbol.equals(pop)){
                    popIndex= f;
                }
                f++;
            }

            int popIndexMem = popIndex;

            if(inputIndex!=numberOfVariablesInInput-1) {
                if (transitionMatrix.getMatrixValues(currentState.getNumber() - 1, inputIndex * numberOfVariablesInStack + popIndex) != null) {
                    String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber() - 1, inputIndex * numberOfVariablesInStack + popIndex);
                    State toState = new State(matrixValue.split("\\s+")[0]);
                    stack.pop();
                    String push = matrixValue.split("\\s+")[1];
                    routeTaken.add(toState);
                    if(!push.equals("ε")){
                        stack.push(push);
                    }
                    return PDA(stringToBeDetected, routeTaken, stack);
                }
                if(popIndex!=numberOfVariablesInStack-1){
                    popIndex = numberOfVariablesInStack-1;
                    if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex) != null){
                        String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex);
                        State toState = new State(matrixValue.split("\\s+")[0]);
                        String push = matrixValue.split("\\s+")[1];
                        routeTaken.add(toState);
                        if(!push.equals("ε")){
                            stack.push(push);
                        }
                        return PDA(stringToBeDetected, routeTaken, stack);
                    }
                }
            }
            popIndex = popIndexMem;
            stringToBeDetected = input+stringToBeDetected;
            if (transitionMatrix.getMatrixValues(currentState.getNumber() - 1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex) != null) {
                String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber() - 1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex);
                State toState = new State(matrixValue.split("\\s+")[0]);
                stack.pop();
                String push = matrixValue.split("\\s+")[1];
                routeTaken.add(toState);
                if(!push.equals("ε")){
                    stack.push(push);
                }
                return PDA(stringToBeDetected, routeTaken, stack);
            }
            if(popIndex!=numberOfVariablesInStack-1){
                popIndex = numberOfVariablesInStack-1;
                if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex) != null){
                    String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex);
                    State toState = new State(matrixValue.split("\\s+")[0]);
                    String push = matrixValue.split("\\s+")[1];
                    routeTaken.add(toState);
                    if(!push.equals("ε")){
                        stack.push(push);
                    }
                    return PDA(stringToBeDetected, routeTaken, stack);
                }
            }
            State deadEnd = new State("d1");
            routeTaken.add(deadEnd);
            return routeTaken;
        }
        else if(stringToBeDetected.length()==1){
            String input = stringToBeDetected;
            stringToBeDetected = "";
            int inputIndex=0;
            int f=0;
            for (String symbol: inputAlphabet){
                if (symbol.equals(input)){
                    inputIndex= f;
                }
                f++;
            }
            State currentState = routeTaken.get(routeTaken.size()-1);
            String pop = stack.popControl();
            int popIndex = 0;
            f=0;
            for (String symbol: stackAlphabet){
                if (symbol.equals(pop)){
                    popIndex= f;
                }
                f++;
            }
            int popIndexMem = popIndex;

            if(inputIndex!=numberOfVariablesInInput-1) {

                if (transitionMatrix.getMatrixValues(currentState.getNumber() - 1, inputIndex * numberOfVariablesInStack + popIndex) != null) {
                    String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber() - 1, inputIndex * numberOfVariablesInStack + popIndex);
                    State toState = new State(matrixValue.split("\\s+")[0]);
                    stack.pop();
                    String push = matrixValue.split("\\s+")[1];
                    routeTaken.add(toState);
                    if(!push.equals("ε")){
                        stack.push(push);
                    }
                    return PDA(stringToBeDetected, routeTaken, stack);
                }
                if(popIndex!=numberOfVariablesInStack-1){
                    popIndex = numberOfVariablesInStack-1;
                    if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex) != null){
                        String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex);
                        State toState = new State(matrixValue.split("\\s+")[0]);
                        String push = matrixValue.split("\\s+")[1];
                        routeTaken.add(toState);
                        if(!push.equals("ε")){
                            stack.push(push);
                        }
                        return PDA(stringToBeDetected, routeTaken, stack);
                    }
                }
            }

            popIndex = popIndexMem;
            //input = empty
            stringToBeDetected = input;
            if (transitionMatrix.getMatrixValues(currentState.getNumber() - 1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex) != null) {
                String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber() - 1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex);
                State toState = new State(matrixValue.split("\\s+")[0]);
                stack.pop();
                String push = matrixValue.split("\\s+")[1];
                routeTaken.add(toState);
                if(!push.equals("ε")){
                    stack.push(push);
                }
                return PDA(stringToBeDetected, routeTaken, stack);
            }
            if(popIndex!=numberOfVariablesInStack-1){
                popIndex = numberOfVariablesInStack-1;
                if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex) != null){
                    String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, (numberOfVariablesInInput-1) * numberOfVariablesInStack + popIndex);
                    State toState = new State(matrixValue.split("\\s+")[0]);
                    String push = matrixValue.split("\\s+")[1];
                    routeTaken.add(toState);
                    if(!push.equals("ε")){
                        stack.push(push);
                    }
                    return PDA(stringToBeDetected, routeTaken, stack);
                }
            }
            State deadEnd = new State("d1");
            routeTaken.add(deadEnd);
            return routeTaken;
        }else{
            State currentState = routeTaken.get(routeTaken.size()-1);
            String input = "ε";
            int inputIndex = numberOfVariablesInInput-1;
            String pop = stack.popControl();
            int popIndex = 0;
            int f=0;
            for (String symbol: stackAlphabet){
                if (symbol.equals(pop)){
                    popIndex= f;
                }
                f++;
            }

            if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex) != null){
                for (FinalState finalState : goalStates) {
                    if (finalState.getName().equals(routeTaken.get(routeTaken.size() - 1).getName())) {
                        return routeTaken;
                    }
                }
                String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex);
                State toState = new State(matrixValue.split("\\s+")[0]);
                stack.pop();
                String push = matrixValue.split("\\s+")[1];
                routeTaken.add(toState);
                if(!push.equals("ε")){
                    stack.push(push);
                }
                return PDA(stringToBeDetected, routeTaken, stack);
            }

            if(popIndex!=numberOfVariablesInStack-1){
                popIndex = numberOfVariablesInStack-1;
                if(transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex) != null){
                    for (FinalState finalState : goalStates) {
                        if (finalState.getName().equals(routeTaken.get(routeTaken.size() - 1).getName())) {
                            return routeTaken;
                        }
                    }
                    String matrixValue = transitionMatrix.getMatrixValues(currentState.getNumber()-1, inputIndex*numberOfVariablesInStack+popIndex);
                    State toState = new State(matrixValue.split("\\s+")[0]);
                    String push = matrixValue.split("\\s+")[1];
                    routeTaken.add(toState);
                    if(!push.equals("ε")){
                        stack.push(push);
                    }
                    for (FinalState finalState : goalStates) {
                        if (finalState.getName().equals(routeTaken.get(routeTaken.size() - 1).getName())) {
                            return routeTaken;
                        }
                    }
                    return PDA(stringToBeDetected, routeTaken, stack);
                }
            }
            return routeTaken;
        }
    }
}
