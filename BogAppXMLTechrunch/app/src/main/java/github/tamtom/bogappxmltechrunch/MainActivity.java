package github.tamtom.bogappxmltechrunch;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    PlaceHolderFragment taskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            taskFragment = new PlaceHolderFragment();
            getSupportFragmentManager().beginTransaction().add(taskFragment, "MyFragment");
        } else {
            taskFragment = (PlaceHolderFragment) getSupportFragmentManager().findFragmentByTag("MyFragment");
        }
        taskFragment.startTask();
    }

    public static class PlaceHolderFragment extends Fragment {
        TechCrunchTask downloadTask;

        public PlaceHolderFragment() {
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }

        public void startTask() {
            if (downloadTask != null) {
                downloadTask.cancel(true);
            } else {
                downloadTask = new TechCrunchTask();
                downloadTask.execute();
                Log.d("excute", "go1");
            }
        }
    }

    public static class TechCrunchTask extends AsyncTask<Void, Void, ArrayList< HashMap<String,String>>> {


        @Override
        protected ArrayList< HashMap<String,String>> doInBackground(Void... params) {
            String downloadUrl = "http://feeds.feedburner.com/techcrunch/android?format=xml";
            ArrayList< HashMap<String,String>> result = null;
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                Log.d("excute", "go2");
              result =   processXML(inputStream);

            } catch (Exception e) {
                Log.e("excption", e + "");
            }
            return result;
        }

        public ArrayList< HashMap<String,String>> processXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputStream);
            Element rootElement = xmlDocument.getDocumentElement();
            Log.d("root is ", rootElement.getTagName());
            NodeList itemsList = rootElement.getElementsByTagName("item");
            Node currentItem = null;
            NodeList itemChildren = null;
            Node currentChild = null;
            NamedNodeMap mediaThumbnailAttributes = null;
            Node currentAttribute = null;
            ArrayList< HashMap<String,String>> result = new ArrayList<>();
            for (int i = 0; i < itemsList.getLength(); i++) {
                currentItem = itemsList.item(i);
                int counter = 0;
                HashMap<String,String> currentMap = new HashMap<>();
                itemChildren = currentItem.getChildNodes();
                for (int j = 0; j < itemChildren.getLength(); j++) {
                    currentChild = itemChildren.item(j);
                    if (currentChild.getNodeName().equalsIgnoreCase("title")) {
                        currentMap.put("title",currentChild.getTextContent());

                    }
                    if (currentChild.getNodeName().equalsIgnoreCase("pubDate")) {
                        currentMap.put("pubDate",currentChild.getTextContent());
                    }
                    if (currentChild.getNodeName().equalsIgnoreCase("description")) {
                        currentMap.put("description",currentChild.getTextContent());

                    }
                    if (currentChild.getNodeName().equalsIgnoreCase("media:thumbnail")) {


                        mediaThumbnailAttributes = currentChild.getAttributes();
                        for (int k = 0; k < mediaThumbnailAttributes.getLength(); k++) {
                            currentAttribute = mediaThumbnailAttributes.item(k);
                            if (currentAttribute.getNodeName().equalsIgnoreCase("url")) {

                                counter++;
                                if (counter == 2) {
                                    Log.d("weeee heeerr","urlr");
                                    currentMap.put("url",currentAttribute.getTextContent());
                                }
                            }
                        }
                    }
                }
                result.add(currentMap);
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
          for(HashMap<String,String> m : result){
              Log.d("title",m.get("title"));
              Log.d("url",m.get("url")+"");
              Log.d("description",m.get("description"));
          }
        }
    }
}
