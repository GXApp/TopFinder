package com.example.mayorgag4.topfinder;

import android.app.Activity;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;
import java.util.List;

import android.view.View;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import android.view.WindowManager;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.AdapterView;


public class ResultsActivity extends Activity {

    public static final String SEARCH = "search";
    private String mPlatform="Android";

    private CardScrollView mCardScroller;
    private List<CardBuilder> mCards;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        mCardScroller = new CardScrollView(this);
        mCards = new ArrayList<CardBuilder>();

        if(getIntent().hasExtra(SEARCH)){
            mPlatform = getIntent().getStringExtra(SEARCH);
        }

        findDevelopers(mPlatform);
        mCardScroller.setAdapter(new DeveloperAdapter(mCards));

        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openOptionsMenu();
            }});

        mGestureDetector = createGestureDetector(this);
        setContentView(mCardScroller);
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu){
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
            getMenuInflater().inflate(R.menu.developers, menu);
            return true;
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    private void findDevelopers(String platform){
        for (int i=1; i<=10; i++){
            // CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

            // The CardBuilder.Layout.COLUMNS layout shows an image mosaic or
            // icon on the left side of the card and text on the right side.

            // Specify how the card's content should be laid out.

            CardBuilder card = new CardBuilder(this, CardBuilder.Layout.COLUMNS);

            card.setText(platform+" "+Integer.toString(i));
            card.setTimestamp(platform);
            card.setIcon(R.drawable.ic_person_50);

            // card.setAttributionIcon(R.drawable.ic_person_50);

            mCards.add(card);
        }
        mCardScroller.setSelection(0);
    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);

        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    openOptionsMenu();
                    return true;
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
                    return true;
                } else if (gesture == Gesture.SWIPE_DOWN){
                    finish();
                }
                return false;
            }
        });

        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });

        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
                return true;
            }
        });

        return gestureDetector;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS || featureId ==  Window.FEATURE_OPTIONS_PANEL) {
            switch (item.getItemId()) {
                case R.id.developer_fav:
                    Toast.makeText(getApplicationContext(), "Select", Toast.LENGTH_LONG).show();
                    break;
                case R.id.go_back:
                    break;
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }
}