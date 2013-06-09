package tw.iccl.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.actionbarsherlock.R;

public class Sensor {
	/***	Debugging	***/
	private static final String TAG = "PullServer";
	private static final boolean D = true;
	
	private Context mContext;
	
	public Sensor(Context context) {
		this.mContext = context;
	}
	
	public void omTemperature() {
		RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.RelativeMain1);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(A_My_Galaxy.this,C_Student_Book_Planet.class), 0);
            }
        });
	}
}
