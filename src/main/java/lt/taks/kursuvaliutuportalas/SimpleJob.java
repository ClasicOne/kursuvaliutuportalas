package lt.taks.kursuvaliutuportalas;

import lt.taks.kursuvaliutuportalas.entiry.*;
import lt.taks.kursuvaliutuportalas.repository.CurrencyTimelineRepo;
import lt.taks.kursuvaliutuportalas.repository.FxRatesRepo;
import lt.taks.kursuvaliutuportalas.repository.LastUpdateTimeRepo;
import lt.taks.kursuvaliutuportalas.services.RestService;
import org.quartz.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SimpleJob implements Job {

    private RestService restService = new RestService(new RestTemplateBuilder());

    private FxRatesRepo fxRatesRepo;
    private CurrencyTimelineRepo currencyTimelineRepo;
    private LastUpdateTimeRepo lastUpdateTimeRepo;


    @Override
    public void execute(JobExecutionContext jobExecutionContext)  {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        List<FxRates> fxRates;
        List<ListCodes> listCodes = null;
        List<CurrencyTimeline> currencyTimelineList;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.MONTH, -1);
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String date2 = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        getRepo(jobExecutionContext);


        List<LastUpdateTime> lastUpdateTimes = lastUpdateTimeRepo.findAll();
//        System.out.println(lastUpdateTimes.size());

//        System.out.println(.get(0).getDate().toString());
        if(new SimpleDateFormat("yyyy-MM-dd").format(lastUpdateTimeRepo.findAll().get(0).getDate()).equals(date1)
                && lastUpdateTimeRepo.findFirstByDate(Date.valueOf(date1)) != null)
            return;
        else {
            lastUpdateTimeRepo.deleteAll();
            lastUpdateTimeRepo.save(new LastUpdateTime(Date.valueOf(date1)));
        }
//            listCodes = restService.getObjectList("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU");
////            System.out.println(listCodes.size());
//            if (listCodes.size() > 0)
//                fxRatesRepo.saveAll(listCodes);
        try {
            listCodes= restService.getAllList("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=EU");
            for (ListCodes list:listCodes) {
                currencyTimelineList = restService.getTimelineOfCurrency("http://www.lb.lt/webservices/FxRates/FxRates.asmx/getFxRatesForCurrency?tp=EU&ccy="
                        +list.getCode()+"&dtFrom="+date2+"&dtTo="+date1);
                currencyTimelineRepo.saveAll(currencyTimelineList);

                        Thread.sleep(500);
            }
        } catch (ParseException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getRepo(JobExecutionContext jobExecutionContext) {
        try {
            SchedulerContext schedulerContext = jobExecutionContext.getScheduler().getContext();
            fxRatesRepo = (FxRatesRepo) schedulerContext.get("fx");
            currencyTimelineRepo = (CurrencyTimelineRepo) schedulerContext.get("time");
            lastUpdateTimeRepo = (LastUpdateTimeRepo) schedulerContext.get("date");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
