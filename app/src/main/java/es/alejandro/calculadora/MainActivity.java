package es.alejandro.calculadora;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

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
        int operatorCount = 0;
        ArrayList<Character> simpleOperators = new ArrayList<>();
        ArrayList<Character> priorityOperators = new ArrayList<>();
        boolean hasPriorityOperation = false;

        for (int i = 0; i< operations.length(); i++){
            if (isOperator(operations.charAt(i))){
                operatorCount++;
                if (operations.charAt(i) == '+' || operations.charAt(i) == '-')
                    simpleOperators.add(operations.charAt(i));
                if (operations.charAt(i) == 'x' || operations.charAt(i) == '/') {
                    hasPriorityOperation = true;
                    priorityOperators.add(operations.charAt(i));
                }
            }
        }

        String[] simpleValues = operations.split("[-+]", operatorCount+1);

        if (hasPriorityOperation){
            int priorityCounter = 0;
            for (int i = 0; i < simpleValues.length; i++){
                String[] priorityValues = simpleValues[i].split("[x/%]",2);
                if (priorityValues.length == 2){
                    priorityCounter++;
                    int firstNum = Integer.parseInt(priorityValues[0]);
                    int secondNum = Integer.parseInt(priorityValues[1]);

                    switch (priorityOperators.get(priorityCounter-1)){
                        case 'x':
                            simpleValues[i] = String.valueOf(firstNum*secondNum);
                            break;
                        case '/':
                            simpleValues[i] = String.valueOf(firstNum/secondNum);
                            break;
                        case '%':
                            simpleValues[i] = String.valueOf(firstNum%secondNum);
                    }
                }
            }
        }


        int firstNumber = Integer.parseInt(simpleValues[0]);
        int secondNumber = 0;
        int count = 0;
        for (int i = 1; i< simpleValues.length; i++){
            secondNumber = Integer.parseInt(simpleValues[i]);

            char operator = simpleOperators.get(count);

            switch (operator){
                case '+':
                    firstNumber = firstNumber+secondNumber;
                    break;
                case '-':
                    firstNumber = firstNumber-secondNumber;
                    break;
                case 'x':
                    firstNumber = firstNumber*secondNumber;
                    break;
                case '/':
                    firstNumber = firstNumber/secondNumber;
                    break;
            }
            count++;
        }

        return String.valueOf(firstNumber);
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
