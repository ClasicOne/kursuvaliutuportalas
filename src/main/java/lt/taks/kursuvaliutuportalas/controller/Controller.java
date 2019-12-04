package lt.taks.kursuvaliutuportalas.controller;


import lt.taks.kursuvaliutuportalas.KursuvaliutuportalasApplication;
import lt.taks.kursuvaliutuportalas.entiry.CurrencyTimeline;
import lt.taks.kursuvaliutuportalas.repository.CurrencyTimelineRepo;
import lt.taks.kursuvaliutuportalas.repository.FxRatesRepo;
import lt.taks.kursuvaliutuportalas.repository.LastUpdateTimeRepo;
import lt.taks.kursuvaliutuportalas.services.RestService;
import org.quartz.SchedulerException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Controller
public  class Controller {
    private RestService restService;
    private FxRatesRepo fxRatesRepo;
    private CurrencyTimelineRepo currencyTimelineRepo;
    private LastUpdateTimeRepo lastUpdateTimeRepo;

    public Controller(RestService restService, FxRatesRepo fxRatesRepo, CurrencyTimelineRepo currencyTimelineRepo, LastUpdateTimeRepo lastUpdateTimeRepo) throws SchedulerException {
        this.restService = restService;
        this.fxRatesRepo = fxRatesRepo;
        this.currencyTimelineRepo = currencyTimelineRepo;
        this.lastUpdateTimeRepo = lastUpdateTimeRepo;
        KursuvaliutuportalasApplication.Scheduler(fxRatesRepo, currencyTimelineRepo, lastUpdateTimeRepo);

    }


    @RequestMapping(value = {"/", ""}, method = {RequestMethod.GET, RequestMethod.POST})
    public String show(Model model, HttpServletRequest request) throws ParseException {
//        model.addAttribute("json2", restService.getPostsPlainJSON("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU"));
        model.addAttribute("json", restService.getAllList("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String selectedCode = request.getParameter("name");
        String cSeletedCode = request.getParameter("convert");
        Float numblerInput = null;
        System.out.println(cSeletedCode);
        if( request.getParameter("input") != null){
            numblerInput = Float.parseFloat(request.getParameter("input"));
            List<CurrencyTimeline> currencyTimelineList = currencyTimelineRepo.getAllByCode(cSeletedCode);
            currencyTimelineList.sort(Comparator.comparing(CurrencyTimeline::getTime));
            Float result = numblerInput * currencyTimelineList.get(currencyTimelineList.size()-1).getValue();
            System.out.printf("%s %s %s %s%n",
                    currencyTimelineList.get(currencyTimelineList.size() - 1).getValue(),
                    currencyTimelineList.get(currencyTimelineList.size() -1).getTime(),
                    currencyTimelineList.get(0).getValue(),
                    currencyTimelineList.get(0).getTime()
            );
            String strResult = "Rezultatas: "+result +" " +  cSeletedCode + " Kurstas : "
                    + currencyTimelineList.get(currencyTimelineList.size()-1).getValue();
            model.addAttribute("result", strResult);
        }




        if (selectedCode != null ){
//            model.addAttribute("dataPointsList",
//                    restService.getTimelineOfCurrency("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy="
//                            +selectedCode+"&dtFrom="+date2+"&dtTo="+date1));
            model.addAttribute("dataPointsList", currencyTimelineRepo.getAllByCode(selectedCode));

        }
        model.addAttribute("sCode", selectedCode);
        System.out.println(selectedCode);
        return "web";
    }

}
