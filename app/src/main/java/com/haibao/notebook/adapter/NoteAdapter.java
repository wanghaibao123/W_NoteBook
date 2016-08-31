package com.haibao.notebook.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibao.notebook.R;
import com.haibao.notebook.entity.NoteEntity;
import com.haibao.notebook.utils.TimeUtils;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by whb on 2016/8/22.
 * Email 18720982457@163.com
 */
public class NoteAdapter extends BaseRecyclerViewAdapter<NoteEntity> implements Filterable {
    private List original;
    private int upDownFactor = 1;
    private boolean isShowScaleAnimate = true;
    public NoteAdapter(List<NoteEntity> list) {
        super(list);

    }

    public void setList(List list){
        original=new ArrayList(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        NoteEntity noteEntity=list.get(position);
        NoteViewHolder noteViewHolder= (NoteViewHolder) holder;
        noteViewHolder.titleText.setText(noteEntity.getTitleName());
        noteViewHolder.contentText.setText(noteEntity.getContent());
        noteViewHolder.time.setText(TimeUtils.getConciseTime(Long.parseLong(noteEntity.getTime())));
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.tv_titleText)
        TextView titleText;
        @ViewInject(R.id.tv_contentText)
        TextView contentText;
        @ViewInject(R.id.tv_time)
        TextView time;
        @ViewInject(R.id.ib_button)
        ImageView more;
        public NoteViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this, itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return new NoteFilter(this,original);
    }

    static class NoteFilter extends Filter{
        private NoteAdapter noteAdapter;
        private List<NoteEntity> original=new ArrayList<>();
        private List<NoteEntity> fileterList;
        public  NoteFilter(NoteAdapter noteAdapter,List<NoteEntity> original){
            super();
            this.noteAdapter=noteAdapter;
            this.original.addAll(original);
            this.fileterList=new ArrayList();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            fileterList.clear();
            FilterResults filterResults=new FilterResults();
            if(charSequence.length()==0){
                fileterList.addAll(original);
            }else{
                for (NoteEntity note:original){
                    if (note.getContent().contains(charSequence) || note.getTitleName().contains(charSequence)) {
                        fileterList.add(note);
                    }
                }
            }
            filterResults.values=fileterList;
            filterResults.count=fileterList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteAdapter.list.clear();
            noteAdapter.list.addAll((ArrayList<NoteEntity>) filterResults.values);
            noteAdapter.notifyDataSetChanged();
        }
    }

    protected Animator[] getAnimators(View view) {
        if (view.getMeasuredHeight() <=0 || isShowScaleAnimate){
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "translationY", upDownFactor * 1.5f * view.getMeasuredHeight(), 0)
        };
    }
}
