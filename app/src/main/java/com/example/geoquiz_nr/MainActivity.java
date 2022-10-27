package com.example.geoquiz_nr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.Gravity;
import android.content.Intent;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "save_answer";
    private static final String KEY_CHEATER = "is_cheater";
    private static final String KEY_COUNT_ANSWERS = "count_answers";
    private static final String KEY_RIGHT_ANSWERS = "right_answers";

    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;

    private Button mNextButton;
    private Button mPreviousButton;
    private Button mCheatButton;
    private boolean mIsCheater[] = new boolean[6];

    private ImageButton mImgNextButton;
    private ImageButton mImgPreviousButton;

    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    private boolean mSaveAnswers[] = new boolean[6];
    private int mCountAnswers;
    private double mRightAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate(Bundle) called");

        //SAVE DATA'S
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mSaveAnswers = savedInstanceState.getBooleanArray(KEY_ANSWER);
            mIsCheater = savedInstanceState.getBooleanArray(KEY_CHEATER);
            mCountAnswers = savedInstanceState.getInt(KEY_COUNT_ANSWERS);
            mRightAnswers = savedInstanceState.getInt(KEY_RIGHT_ANSWERS);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();

        System.out.println("Vivod quest: " + question);
        mQuestionTextView.setText(question);

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

//                Toast myToast = Toast.makeText(MainActivity.this,
//                        R.string.incorrect_toast,
//                        Toast.LENGTH_SHORT);
//                myToast.setGravity(Gravity.TOP, 0, 0);
//                myToast.show();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        EnabledButtons();

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                System.out.println(mCurrentIndex);
                System.out.println(mQuestionBank.length);
                updateQuestion();
            }
        });

        mPreviousButton = (Button) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex == -1) {
                    mCurrentIndex = 5;
                }
                updateQuestion();
            }
        });

//    mImgPreviousButton
        mImgPreviousButton = (ImageButton) findViewById(R.id.previous_Img_button);
        mImgPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex == -1) {
                    mCurrentIndex = 5;
                }
                updateQuestion();
            }
        });

        mImgNextButton = (ImageButton) findViewById(R.id.next_Img_button);
        mImgNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
//                OLD_ Intent intent = new Intent(MainActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                //OLD_ startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
        }
    }

    //в отдельный метод?
    private void EnabledButtons() {
        if (mSaveAnswers[mCurrentIndex] == true) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    private void updateQuestion() {
//        Log.d(TAG, "Updating question text", new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        EnabledButtons();
    }

    private void checkAnswer(boolean userPressedTrue) {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
//        int countAnswers = 0;

        mCountAnswers++;
        System.out.println("oooo: "+ mCountAnswers);

        if (mIsCheater[mCurrentIndex] == true) {
            messageResId = R.string.judgment_toast;
            mRightAnswers++;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mRightAnswers++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        if (mCountAnswers == 6) {

            mRightAnswers = mRightAnswers / 0.06;

            System.out.println("Правильных ответов: " + mRightAnswers);

            Toast ResultToast = Toast.makeText(MainActivity.this,
                    "Правильных ответов: " + mRightAnswers,
                        Toast.LENGTH_SHORT);
            ResultToast.setGravity(Gravity.TOP, 0, 0);
            ResultToast.show();
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();

        mSaveAnswers[mCurrentIndex] = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBooleanArray(KEY_ANSWER, mSaveAnswers);
        savedInstanceState.putBooleanArray(KEY_CHEATER, mIsCheater);
        savedInstanceState.putInt(KEY_COUNT_ANSWERS, mCountAnswers);
        savedInstanceState.putDouble(KEY_RIGHT_ANSWERS, mRightAnswers);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

}