package com.example.geoquiz_nr;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.animation.Animator;
import android.view.ViewAnimationUtils;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;

public class CheatActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
//    private static final String KEY_COUNT_SHOWN= "count_shown";

    private static final String EXTRA_ANSWER_IS_TRUE ="com.example.geoquiz_nr.answer_is_true";;
    private static final String EXTRA_ANSWER_SHOWN ="com.example.geoquiz_nr.answer_shown";
//    private static final String EXTRA_COUNT_SHOWN ="com.example.geoquiz_nr.answer_is_true";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private boolean mCheckRotate;
//    private int mCountCheatButtonPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mCheckRotate = savedInstanceState.getBoolean(KEY_INDEX);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCountCheatButtonPress++;
                ShowTextAnswer();
                setAnswerShownResult(true);
                mShowAnswerButton.setEnabled(false);

                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (mCheckRotate){
            ShowTextAnswer();
            mShowAnswerButton.setEnabled(false);
            setAnswerShownResult(mCheckRotate);
        }

        TextView APIVersionTextView = (TextView) findViewById(R.id.API_Version_Text);
        APIVersionTextView.setText("The current API level of your device is " + Build.VERSION.SDK_INT);
    }

    private void ShowTextAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }
    private void setAnswerShownResult(boolean isAnswerShown) {
        mCheckRotate = true;
        Intent data = new Intent();
        Intent data2 = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
//        data2.putExtra(EXTRA_COUNT_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
//        setResult(RESULT_OK, data2);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    //ПРОВЕРИТЬ будет ли работать в главном классе
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

//    public static boolean GetDigitPressed(Intent result) {
//        return result.getIntExtra(EXTRA_ANSWER_SHOWN, 0);
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        Log.i(TAG, "onSaveInstanceState");

        System.out.println(mCheckRotate);
        savedInstanceState.putBoolean(KEY_INDEX, mCheckRotate);
//        savedInstanceState.putInt(KEY_COUNT_SHOWN, mCountCheatButtonPress);
//        if (mCheckRotate == true) {
//            setAnswerShownResult(true);
//            System.out.println("zapustilos");
//        }
//        savedInstanceState.putBooleanArray(KEY_INDEX2, mSaveAnswers);
//        savedInstanceState.putBoolean(KEY_INDEX3, mIsCheater);
    }
}
