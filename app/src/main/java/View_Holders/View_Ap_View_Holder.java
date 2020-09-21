package View_Holders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shopkeeper.R;



public class View_Ap_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textviewpname,textviewquentity;
    public RelativeLayout rl1;


    public View_Ap_View_Holder(@NonNull View itemView)
    {
        super(itemView);

        textviewpname = (itemView).findViewById(R.id.appname);
        textviewquentity = (itemView).findViewById(R.id.approductquentity);
        rl1 =(itemView).findViewById(R.id.approductname);
    }



    @Override
    public void onClick(View v)
    {

    }
}
