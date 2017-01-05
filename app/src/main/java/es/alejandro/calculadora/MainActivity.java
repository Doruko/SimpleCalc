package es.alejandro.calculadora;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean isOperatorLast = false;
    boolean portrait = true;


    int copyNumber;

    private char operation;

    private String operations = "";
    private String result = "";

    private int resultInt;
    private int actualNumber;

    private TextView operationsTv;
    private TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsTv = (TextView) findViewById(R.id.operationsTV);
        resultTv = (TextView) findViewById(R.id.resultTV);
    }

    public void numClick(View v){

        String pushedString = ((TextView)findViewById(v.getId())).getText().toString();

        operations = operations.concat(pushedString);
        operationsTv.setText(operations);

        isOperatorLast = false;
    }

    public void operatorClick(View v){

        String pushedString = ((TextView)findViewById(v.getId())).getText().toString();

        if (!isOperatorLast){
            operations = operations.concat(pushedString);
            operationsTv.setText(operations);
            isOperatorLast = true;

            switch (v.getId()){
                case R.id.calc_sum:
                    operation = '+';
                    break;
                case R.id.calc_multiply:
                    operation = 'x';
                    break;
                case R.id.calc_divide:
                    operation = '/';
                    break;
                case R.id.calc_minus:
                    operation = '-';
                    break;
                case R.id.calc_module:
                    break;
                default:
                    break;
            }
        }
    }

    public void specialClick(View v){
        switch (v.getId()){
            case R.id.calc_equal:
                resultTv.setText(calculateResult(operations));
                break;
            case R.id.calc_clean:
                resetContent();
                break;
            case R.id.calc_copy:
                break;
            case R.id.calc_paste:
                break;
        }
    }

    private String calculateResult(String operations) {
        String result = "";
        char c = 0;
        boolean firstOperator = true;
        int operationPos = 0;
        boolean thereAreOperations = true;
        String operationToRepeat = "";
        int operationsCounter = 0;
        boolean operationDone = false;

        for (int i = 0; i < operations.length(); i++){
            c = operations.charAt(i);
            if (isOperator(c)){
                operationsCounter++;
                if (firstOperator) {
                    operationPos = i;
                    firstOperator = false;
                }

                if (c == 'x' || c == '/') {
                    if (!operationDone) {
                        operationPos = i;

                        operationToRepeat = calculate(operations, c, operationPos);
                        operationDone = true;
                    }
                }
            }
        }

        if (!operationDone){
            operationToRepeat = calculate(operations, c, operationPos);
        }

        if (operationsCounter > 1){
            calculateResult(operationToRepeat);
        }

        return result;
    }

    private String calculate(String operation, char operator, int pos){
        String result = "";
        int res = 0;

        String[] numbers=getNumbersForOperation(operation, pos);
        int firsNum = Integer.parseInt(numbers[0]);
        String firsPart = numbers[1];
        int secondNum = Integer.parseInt(numbers[2]);
        String secondPart = numbers[3];

        result+=firsPart;

        switch (operator){
            case '+':
                res = firsNum + secondNum;
                break;
            case '-':
                res = firsNum - secondNum;
                break;
            case 'x':
                res = firsNum * secondNum;
                break;
            case '/':
                res = firsNum / secondNum;
                break;
            default:
                break;
        }
        result += res;
        result += secondPart;
        return  result;
    }

    private String[] getNumbersForOperation(String operation, int pos){
        String[] parts = new String[4];

        String opFirstPart = operation.substring(0, pos);
        String opSecondPart = operation.substring(pos,operation.length());

        String firstPart = "";
        String secondPart = "";

        String firstOp = "";
        String secondOp = "";

        for (int i = opFirstPart.length(); i>0; i--){
            if (firstOp.equals(""))
                if (isOperator(opFirstPart.charAt(i-1))){
                    String str = opFirstPart.substring(i,opFirstPart.length());
                    firstPart = opFirstPart.substring(0, i);
                    firstOp = str;
                }
        }

        for (int i = 1; i<=opSecondPart.length(); i++){
            if (secondOp.equals("")) {
                if (isOperator(opSecondPart.charAt(i))) {
                    secondOp = opSecondPart.substring(1, i);
                    secondPart = opSecondPart.substring(i, opSecondPart.length());
                } else if (i == opSecondPart.length()) {
                    secondOp = opSecondPart.substring(1, opSecondPart.length());
                    secondPart = opSecondPart.substring(i, opSecondPart.length());
                }
            }

        }

        parts[0] = firstOp;
        parts[1] = firstPart;
        parts[2] = secondOp;
        parts[3] = secondPart;

        return parts;
    }

    private boolean isOperator(char c){
        return c == '+'|| c == '-'|| c == 'x'|| c == '/';
    }


    private void resetContent() {
        resultInt = 0;
        operations = "";
        operationsTv.setText("");
        resultTv.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        createMenu(menu);
        return true;
    }

    private void createMenu(Menu menu) {
        MenuItem item = menu.add(0,0,0,"Rotate");
        {
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
    }

    private boolean menuChoice(MenuItem item){
        switch (item.getItemId()){
            case 0:
                if (portrait){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                portrait = !portrait;
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action) {
            return true;
        }

        return menuChoice(item);
    }
}
