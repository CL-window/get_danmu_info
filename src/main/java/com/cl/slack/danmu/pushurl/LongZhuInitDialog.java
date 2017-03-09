package com.cl.slack.danmu.pushurl;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.cl.slack.danmu.R;

/**
 * Created by slack
 * on 17/3/2 下午6:44.
 */

public class LongZhuInitDialog extends Dialog {

    private EditText mTitle;
    private RecyclerView mRecyclerView;

    public LongZhuInitDialog(Context context) {
        this(context, R.style.selectorDialog);
    }

    public LongZhuInitDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        if(getWindow() != null)
            getWindow().setWindowAnimations(R.style.dialog_anim_style);
        setContentView(R.layout.dialog_longzhu_init);
        mTitle = (EditText) findViewById(R.id.select_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.select_list);

    }

//    public LongZhuInitDialog setSingleChoiceItems() {
//        mSingleSelectArrays = arrays;
//        mRecycleView.setAdapter(new SingleSelectAdapter(getContext()));
//        return this;
//    }
//
//    private class SingleSelectAdapter extends RecyclerView.Adapter<SingleSelectAdapter.RadioViewHolder> {
//
//        protected Context mContext;
//
//        public SingleSelectAdapter(@NonNull Context context) {
//             mContext = context;
//        }
//
//        @Override
//        public RadioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new RadioViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_setting_radio, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(RadioViewHolder holder, int position) {
//            holder.bindView(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mSingleSelectArrays.length;
//        }
//
//        public class RadioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            private TextView name;
//            private ImageView select;
//
//            public RadioViewHolder(View itemView) {
//                super(itemView);
//                name = (TextView) findViewById(R.id.radio_name);
//                select = (ImageView) findViewById(R.id.radio_image);
//            }
//
//            public void bindView(int position) {
//
//                name.setText(mSingleSelectArrays[position]);
//                if (position == mCurrentSelect) {
//                    select.setImageResource(R.drawable.setting_select);
//                } else {
//                    select.setImageResource(R.drawable.setting_unselect);
//                }
//
//                itemView.setOnClickListener(this);
//            }
//
//            private View findViewById(@IdRes int id) {
//                return itemView.findViewById(id);
//            }
//
//
//            @Override
//            public void onClick(View v) {
////                D.i("slack", "click :" + getAdapterPosition());
//                if(mRadioSelectListener != null){
//                    mRadioSelectListener.onRadioSelect(getAdapterPosition(),mSingleSelectArrays[getAdapterPosition()]);
//                }
//                dismiss();
//            }
//        }
//    }

    public interface RadioSelectListener{
        void onRadioSelect(int pos,String selectName);
    }
}
