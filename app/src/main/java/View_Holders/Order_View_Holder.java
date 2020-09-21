package View_Holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shopkeeper.R;


public class Order_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView textusername,textphone,textprice,textdatetime,textcity;
    public Button showallproduct;

    public Order_View_Holder(@NonNull View itemView)
    {
        super(itemView);

        textusername = (itemView).findViewById(R.id.aousername);
        textphone = (itemView).findViewById(R.id.aophone);
        textprice = (itemView).findViewById(R.id.aototalprice);
        textdatetime = (itemView).findViewById(R.id.aodatetime);
        textcity = (itemView).findViewById(R.id.aoaddcity);

        showallproduct = (itemView).findViewById(R.id.aoshowallprobtn);
    }



    @Override
    public void onClick(View v)
    {

    }
}
