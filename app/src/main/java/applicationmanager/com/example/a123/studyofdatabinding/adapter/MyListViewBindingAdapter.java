package applicationmanager.com.example.a123.studyofdatabinding.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;

import applicationmanager.com.example.a123.studyofdatabinding.BR;
import applicationmanager.com.example.a123.studyofdatabinding.R;
import applicationmanager.com.example.a123.studyofdatabinding.javaBean.Person;

public class MyListViewBindingAdapter extends BaseAdapter {
    private LinkedList<Person> people;
    private Context mContext;

    public MyListViewBindingAdapter(LinkedList<Person> people, Context mContext) {
        this.people = people;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewDataBinding binding = null;
        if (view == null) {
            Person person = people.get(i);
            if (person != null) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item, viewGroup, false);
                binding.setVariable(BR.personItem, person);
            }
        } else {
            binding = DataBindingUtil.getBinding(view);
        }

        return binding.getRoot();
    }
}
