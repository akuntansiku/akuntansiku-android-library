package id.co.akuntansiku.utils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.adapter.DateFilterAdapter;
import id.co.akuntansiku.utils.model.DataDateFilter;

import static id.co.akuntansiku.utils.Helper.setDate;
import static id.co.akuntansiku.utils.Helper.setFirstDayLastMonth;
import static id.co.akuntansiku.utils.Helper.setFirstDayThisMonth;
import static id.co.akuntansiku.utils.Helper.setLastDayLastMonth;
import static id.co.akuntansiku.utils.Helper.setLastDayThisMonth;

public class DateFilter {
    ArrayList<DataDateFilter> dataDateFilter = new ArrayList<>();
    String first_date;
    String last_date;
    Activity activity;
    Spinner spinner;
    DateFilterAdapter dateFilterAdapter;

    public FilterDate mFilterDate;
    public interface FilterDate {
        void onChangeFilterDate(String from, String to);
    }


    public DateFilter(Activity activity, Spinner mSpinner, final FilterDate mFilterDate){
        this.mFilterDate = mFilterDate;
        this.activity = activity;
        this.spinner = mSpinner;
        dataDateFilter.add(new DataDateFilter(0, "Hari ini", setDate(0), setDate(0)));
        dataDateFilter.add(new DataDateFilter(0, "Kemarin", setDate(-1), setDate(-1)));
        dataDateFilter.add(new DataDateFilter(0, "7 hari terkahir", setDate(-7), setDate(-1)));
        dataDateFilter.add(new DataDateFilter(0, "30 hari terkahir", setDate(-30), setDate(-7)));
        dataDateFilter.add(new DataDateFilter(0, "Bulan ini", setFirstDayThisMonth(), setLastDayThisMonth()));
        dataDateFilter.add(new DataDateFilter(0, "Bulan lalu", setFirstDayLastMonth(), setLastDayLastMonth()));
//        dataDateFilter.add(new DataDateFilter(0, "Selama ini", "0", "1"));
//        dataDateFilter.add(new DataDateFilter(7, "Custom", " ", " "));

        dateFilterAdapter = new DateFilterAdapter(activity, R.layout.akuntansiku_spinner_date, dataDateFilter);
        spinner.setAdapter(dateFilterAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i == 7) {
//                    openDateRangePicker();
//                }
                first_date = dataDateFilter.get(i).getFirst_date() + " 00:00:00";
                last_date = dataDateFilter.get(i).getLast_date() + " 24:00:00";
                mFilterDate.onChangeFilterDate(first_date, last_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }

//    private void openDateRangePicker() {
//        final SublimePickerFragment pickerFrag = new SublimePickerFragment();
//        pickerFrag.setCallback(new SublimePickerFragment.Callback() {
//            @Override
//            public void onCancelled() {
//                Toast.makeText(activity, "cancelled",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDateTimeRecurrenceSet(final SelectedDate selectedDate, int hourOfDay, int minute,
//                                                SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
//                                                String recurrenceRule) {
//
//                @SuppressLint("SimpleDateFormat")
//                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                String startdate = formatDate.format(selectedDate.getStartDate().getTime());
//                String enddate = formatDate.format(selectedDate.getEndDate().getTime());
//                dataDateFilter.get(7).setFirst_date(startdate);
//                dataDateFilter.get(7).setLast_date(enddate);
//
//                dateFilterAdapter.notifyDataSetChanged();
//                spinner.setAdapter(dateFilterAdapter);
//                spinner.setSelection(7);
//            }
//        });
//
//        // ini configurasi agar library menggunakan method Date Range Picker
//        SublimeOptions options = new SublimeOptions();
//        options.setCanPickDateRange(true);
//        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("SUBLIME_OPTIONS", options);
//        pickerFrag.setArguments(bundle);
//
//        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
//        pickerFrag.show(getFragmentManager(), "SUBLIME_PICKER");
//    }

}
