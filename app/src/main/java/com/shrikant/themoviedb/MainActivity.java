package com.shrikant.themoviedb;

import com.shrikant.themoviedb.fragments.FavouriteMovieFragment;
import com.shrikant.themoviedb.fragments.NowShowingMoviesFragment;
import com.shrikant.themoviedb.fragments.PopularMoviesFragment;
import com.shrikant.themoviedb.fragments.SavedMoviesFragment;
import com.shrikant.themoviedb.fragments.TopRatedMoviesFragment;
import com.shrikant.themoviedb.fragments.UpcomingMoviesFragment;
import com.shrikant.themoviedb.models.Movie;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
    implements PopularMoviesFragment.OnItemSelectedListener,
        SavedMoviesFragment.OnItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nvView)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar ,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        setupDrawerContent(mNavigationView);

        try {
            //Fragment fragment = (Fragment) NowShowingMoviesFragment.class.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, NowShowingMoviesFragment.class.newInstance())
                    .commit();
        } catch (IllegalAccessException | InstantiationException e) {
            Log.e(TAG, "Error in intializing fragment" + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE! Make sure to override the method with only a single `Bundle` argument
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_nowplaying_fragment:
                Log.i(TAG, "First item selected");
                fragmentClass = NowShowingMoviesFragment.class;
                setTitle(R.string.nav_now_playing);
                break;
            case R.id.nav_favourite_fragment:
                Log.i(TAG, "Second item selected");
                fragmentClass = FavouriteMovieFragment.class;
                setTitle(R.string.nav_favourite);
                break;
            case R.id.nav_popular_fragment:
                Log.i(TAG, "Third item selected");
                fragmentClass = PopularMoviesFragment.class;
                setTitle(R.string.nav_popular);
                break;
            case R.id.nav_toprated_fragment:
                Log.i(TAG, "Retrofit item selected");
                fragmentClass = TopRatedMoviesFragment.class;
                setTitle(R.string.nav_toprated);
                break;
            case R.id.nav_upcoming_fragment:
                Log.i(TAG, "Volley item selected");
                fragmentClass = UpcomingMoviesFragment.class;
                setTitle(R.string.nav_upcoming);
                break;
            case R.id.nav_dbflow_fragment:
                Log.i(TAG, "DBFlow item selected");
                fragmentClass = SavedMoviesFragment.class;
                setTitle(R.string.nav_mymovies);
                break;
            default:
                fragmentClass = NowShowingMoviesFragment.class;
                setTitle(R.string.nav_now_playing);
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onPopularMovieSelected(Movie movie) {
        Log.i(TAG, "Saving " + movie.getTitle());
        movie.save();
    }

    @Override
    public void onSavedMovieSelected(Movie movie) {
        Log.i(TAG, "Deleting " + movie.getTitle());
        movie.delete();
    }
}
