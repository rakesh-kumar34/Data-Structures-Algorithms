import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

class Solution {

    public static int calculate(String s) {
        List<String> postFix = findPostFix(s);
        return calc(postFix);
    }

    static List<String> findPostFix(String s) {

        StringBuffer result = new StringBuffer();
        ArrayDeque<Character> st = new ArrayDeque<>();

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            // Escape space
            if (Character.isWhitespace(ch))
                continue;
            // Keep appending digit or letter
            else if (Character.isLetterOrDigit(ch))
                result.append(ch);
            else if (ch == '(')
                st.push(ch);
            else if (ch == ')') {
                // keep popping and appending to result unless you see and open '('
                while (!st.isEmpty() && st.peek() != '(') {
                    result.append("#");
                    result.append(st.pop());
                }
                // Pop '(' out and dont add it to the result
                st.pop();
            } else {
                // Process operator (+, -, *, /)
                // Pop the operator with higher/equal predecende from the top of the stack and
                // append to the result
                while (!st.isEmpty() && precedence(ch) <= precedence(st.peek())) {
                    // Append the delimeter before adding the popped operator to the result
                    result.append('#');
                    result.append(st.pop());
                }
                // Append the delimeter before adding the operator to the result
                result.append('#');
                st.push(ch);
            }
        }
        // 5#4#3#*#+#2
        // Append the remaining operators from the stack into the result
        while (!st.isEmpty()) {
            result.append('#');
            result.append(st.pop());
        }
        // 5#4#3#*#+#2#-

        // At this point we have all the tokens in the result delimeted by #
        String[] str = result.toString().split("#");
        return Arrays.asList(str);
    }

    static int calc(List<String> postfix) {
        // 5 4 3 * + 2 -
        // 5 12 + 2 - (after processing operator *)
        // 17 2 - (after processing operator +)
        // 15 (after processing operator -)
        ArrayDeque<Integer> st = new ArrayDeque<>();
        for (var s : postfix) {
            if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/")) { // operator
                int b = st.pop();
                int a = st.pop();
                int val = evaluate(s, a, b);
                st.push(val);
            } else {
                st.push(Integer.parseInt(s));
            }
        }
        return st.pop();
    }

    static int evaluate(String op, int a, int b) {
        if (op.equals("+")) {
            return a + b;
        }
        if (op.equals("-")) {
            return a - b;
        }
        if (op.equals("*")) {
            return a * b;
        }
        if (op.equals("/")) {
            return a / b;
        }
        return 0;
    }

    static int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        }
        if (op == '*' || op == '/') {
            return 2;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(calculate("(1+(4+5+2)-3)+(6+8)"));
    }
}
