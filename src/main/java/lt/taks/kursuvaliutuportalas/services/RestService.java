package lt.taks.kursuvaliutuportalas.services;

import lt.taks.kursuvaliutuportalas.entiry.CcyAmt;
import lt.taks.kursuvaliutuportalas.entiry.CurrencyTimeline;
import lt.taks.kursuvaliutuportalas.entiry.FxRates;
import lt.taks.kursuvaliutuportalas.entiry.ListCodes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(String url) {
        JSONObject xmlObj = XML.toJSONObject(this.restTemplate.getForObject(url, String.class));
        String jsonPrestty = xmlObj.toString(4);
        System.out.println(jsonPrestty);
        return this.restTemplate.getForObject(url, String.class);
    }

    public List<FxRates> getObjectList (String url) throws ParseException {
        JSONObject xmlObj = XML.toJSONObject(this.restTemplate.getForObject(url, String.class));
//        getPostsPlainJSON(url);
//        JSONArray fxRate = xmlObj.getJSONArray("FxRate");
        JSONObject fxraterObj = xmlObj.getJSONObject("FxRates");
        JSONArray fxrateArr = fxraterObj.getJSONArray("FxRate");
        List<FxRates> arrayList = new ArrayList<>();

        for (int i=0;i<fxrateArr.length();i++){
            FxRates fxRates = new FxRates();
            JSONArray  temp= fxrateArr.getJSONObject(i).getJSONArray("CcyAmt");
            List<CcyAmt> ccyAmts = new ArrayList<>();
            fxRates.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(fxrateArr.getJSONObject(i).getString("Dt")));
            fxRates.setTp(fxrateArr.getJSONObject(i).getString("Tp"));
            for (int y=0;y<temp.length();y++) {
                CcyAmt ccyAmt = new CcyAmt();
                ccyAmt.setAmt(fxrateArr.getJSONObject(i).getJSONArray("CcyAmt").getJSONObject(y).getFloat("Amt"));
                ccyAmt.setCcy(fxrateArr.getJSONObject(i).getJSONArray("CcyAmt").getJSONObject(y).getString("Ccy"));
                ccyAmts.add(ccyAmt);
            }
            fxRates.setCcyAmt(ccyAmts);
            arrayList.add(fxRates);
        }
        return arrayList;
    }
    public List<ListCodes> getAllList(String url) throws ParseException {
        List<FxRates> fxRates = getObjectList(url);
        List<ListCodes> stringList = new ArrayList<>();


        fxRates.forEach(fxRates1 -> {
            ListCodes listCodes = new ListCodes();
            List<CcyAmt> temp ;
            temp = fxRates1.getCcyAmt();
            listCodes.setValue(temp.get(1).getAmt());
            listCodes.setCode(temp.get(1).getCcy());

            stringList.add(listCodes);
        });
        return stringList;
    }

    public List<CurrencyTimeline> getTimelineOfCurrency(String url) throws ParseException {
        JSONObject xmlObj = XML.toJSONObject(this.restTemplate.getForObject(url, String.class));
        List<CurrencyTimeline> currencyTimelines = new ArrayList<>();
        JSONObject fxraterObj = xmlObj.getJSONObject("FxRates");
//        JSONObject jsonObject = fxraterObj.getJSONObject("FxRate");
        JSONArray fxrateArr = fxraterObj.getJSONArray("FxRate");
        for (int i=0;i<fxrateArr.length();i++){
            CurrencyTimeline currencyTimeline = new CurrencyTimeline();
            JSONArray  temp= fxrateArr.getJSONObject(i).getJSONArray("CcyAmt");

            currencyTimeline.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(fxrateArr.getJSONObject(i).getString("Dt")));
            currencyTimeline.setCode(temp.getJSONObject(1).getString("Ccy"));
            currencyTimeline.setValue(temp.getJSONObject(1).getFloat("Amt"));
            currencyTimeline.setTp(fxrateArr.getJSONObject(i).getString("Tp"));
            currencyTimelines.add(currencyTimeline);
        }
        return currencyTimelines;
    }

}
