import java.util.ArrayList;
import java.util.List;

public class Stack {
    List<String> stack;

    public Stack(){
        this.stack = new ArrayList<>();
    }

    public String popControl(){
        if(stack.size()==0){
            return "ε";
        }else {
            return this.stack.get(stack.size() - 1);
        }
    }

    public String pop(){
        if(stack.size()==0){
            return "ε";
        }else {
            String pop = this.stack.get(stack.size()-1);
            this.stack.remove(stack.size()-1);
            return pop;
        }
    }

    public void push(String symbol){
        this.stack.add(symbol);
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i< stack.size(); i++){
            result += stack.get(i);
        }
        return result;
    }
}
