package View_Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopkeeper.R;

public class EP_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname,txtproductdes,txtproductprice;
    public ImageView pdimageview;

    public EP_View_Holder(@NonNull View itemView)
    {
        super(itemView);

        pdimageview = (ImageView) itemView.findViewById(R.id.ocproductimage);
        txtproductname = (TextView) itemView.findViewById(R.id.ocproductname);
        txtproductdes = (TextView) itemView.findViewById(R.id.ocproductdes);
        txtproductprice = (TextView) itemView.findViewById(R.id.ocprice);
    }

    @Override
    public void onClick(View v)
    {

    }
}
