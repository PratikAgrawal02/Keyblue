package com.sih.disaster;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Faq extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Versions> versionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        recyclerView = findViewById(R.id.recyclerView);
        dataIn();

    }

    private void setRecyclerView() {
        versionsAdapter versionsAdapter = new versionsAdapter(versionsList);
        recyclerView.setAdapter(versionsAdapter);
        recyclerView.setHasFixedSize(true);
    }
    public void back(View view){
        finish();
    }

    private void dataIn() {
        versionsList = new ArrayList<>();
        versionsList.add(new Versions("Q. What is Disaster Management ?","Disaster management is a process of effectively preparing for and responding to disasters. It involves strategically organizing resources to lessen the harm that disasters cause. It also involves a systematic approach to managing the responsibilities of disaster prevention, preparedness, response, and recovery."));

        versionsList.add(new Versions("Q. What is the aim of Disaster Management in India ?","In India, the main objective of disaster management is to reduce or avoid the potential loss which may be caused due to a hazard or disaster. Also, so that quick, apt and prompt steps can be taken for an effective recovery. The National Disaster Management Authority (NDMA) is the apex body that is responsible for Disaster Management in India. NDMA is headed by the Prime Minister of India."));

        versionsList.add(new Versions("Q. In how many categories can Disasters be classified ?","Disasters can be classified into the following categories:\n" +
                "•\tWater and Climate Disaster\n" +
                "•\tGeological Disaster\n" +
                "•\tBiological Disaster\n" +
                "•\tIndustrial Disaster\n" +
                "•\tNuclear Disasters\n" +
                "•\tMan-made disasters\n"));

        versionsList.add(new Versions("Q. What are the three stages of Disaster Management ?","The three stages of disaster management are:\n" +
                "•\tPre- disaster stage (preparedness)\n" +
                "•\tEmergency stage (Disaster phase or phase of catastrophe)\n" +
                "•\tPost- disaster stage (Rehabilitation/Recovery)"));

        versionsList.add(new Versions("Q. How are Disasters and Hazards different ?","Hazard is an event that has potential for causing injury/loss of life or damage to property/environment. Disaster is an event that occurs suddenly/unexpectedly in most cases and disrupts the normal course of life in affected area."));

        versionsList.add(new Versions("Q. What's the difference between a \"drill\" and an \"exercise\" ?","A drill is a test of a portion of the response organization (for example, a fire drill tests a facility's firefighting teams and the individuals in the vicinity of the area selected for that particular drill). \n" +
                "An \"exercise\" typically tests many facets of the response organization and often involves close coordination between licensee (onsite) and State and local (offsite) response organizations.\n"));

        //Self Questions

        versionsList.add(new Versions("Q. What is the main objective of KeyBlue?","KeyBlue is a Portal created by Tricolored Technophiles for:\n" +
                "•\tCreating awareness blogs about Disaster Management. \n" +
                "•\tOrganizing mock drills.\n" +
                "•\tProviding status of each of the registered rehabilitation organizations and active rangers.\n" +
                "KeyBlue helps educate people as well as guide them through the crucial phases of disaster management. Further, it also spreads awareness of all aspects (pre-disaster, post-disaster as well as during the disaster) to users via the app and website." +
                "\tThe Technophiles API serves as the backend to the KeyBlue Android App and Website.\n"));

        versionsList.add(new Versions("Q. What is the main focus of KeyBlue?","KeyBlue bridges the connectivity gap between the rescuers and the rescuees through affiliation with NGOs." +
                "Furthermore, it also ensures that users stay interconnected, so that awareness regarding various mishaps in different parts of the country and as well guidance on how to overcome the same can be spread efficiently to the affected population."));

        versionsList.add(new Versions("Q. How is KeyBlue capable of spreading information to the maximum number of people during disaster phase in affected area?","KeyBlue has come up with a portal which uses Bluetooth technology to pass on essential information during the time of emergency." +
                "The mechanism includes setting up of devices in partitions around the affected area, that act as the transmitter of information throughout the partitions."));

        versionsList.add(new Versions("Q. How does KeyBlue promote maximum inclusivity?","KeyBlue Portal is designed to be multi-lingual which furthers its objective of spreading information to a diverse crowd."));

        versionsList.add(new Versions("Q. How does KeyBlue aid during the pre-disaster phase?","KeyBlue uploads blogs and posts frequently on topics such as how one can overcome mishaps that happen during a disaster, and on how to deal with such situations." +
                "Moreover, KeyBlue encourages people to take part in mock-drills and works on creating concrete establishment of connection with resources. " +
                "Further, it establishes connections with NGOs which provide rangers who aid in the time of need in as many areas as possible, as coping with disastrous situations is not a one-man thing."));

        versionsList.add(new Versions("Q. How does KeyBlue assist during the post-disaster phase?","KeyBlue assist during the post-disaster phase in the following ways:\n" +
                "•\tIt gets the details of basic protocols to be followed and further actions to be taken.\n" +
                "•\tIt posts panels providing details of the nearest volunteer available for aid. Further, it also posts details of nearest rescue camp via Bluetooth rendered messages.\n" +
                "•\tIt creates minimum bandwidth domains for smooth communication during emergency.\n" +
                "•\tIt posts timely updates of the situation which enables the users to get a sense of how people can ask for help themselves or aid others.\n" +
                "•\tIts employees contact the NGOs, hospitals, nearest police stations, etc. to provide quick assistance while also providing details to the same about rescuees’ mobile numbers, blood groups etc.\n"));

        versionsList.add(new Versions("Q. How do we educate people about overcoming disasters?","The KeyBlue website hosts a large number of blogs and articles written by the team as well as the volunteers containing a list of precautionary measures for all kinds of mishaps during the disaster." +
                " The articles also include real-life experiences by volunteers who are a part of KeyBlue, an initiative to get real stories out there, in order to enable people to learn from them. " +
                " It is beneficial to people who live in disaster-prone places."));

        versionsList.add(new Versions("Q. How does KeyBlue aid during the emergency stage?","KeyBlue ensures a wide network of connectivity by close affiliation with maximum NGOs and active rangers present in distinct areas. " +
                "During a disaster, the only way to ensure safety of the people stuck in the mishap will be through these NGOs and the voluntary help provided by the rangers present during the disaster."));

        versionsList.add(new Versions("Q. How does KeyBlue App differ from Website?","KeyBlue App provides an in-hand way of user-registration and permits the user to access information through the Bluetooth-driven portal easily, whereas in the KeyBlue Website, user gets an even wider range of information like blogs, articles and information of conduction of mock-drills among many others."));
        setRecyclerView();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}