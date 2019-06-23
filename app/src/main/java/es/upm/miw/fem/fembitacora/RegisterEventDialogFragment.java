package es.upm.miw.fem.fembitacora;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class RegisterEventDialogFragment extends DialogFragment {

   public Dialog onCreateDialog(Bundle savedInstanceState){
        final DetailDeliveryActivity activity = (DetailDeliveryActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle(R.string.dialog_text_reg_event_title)
                .setMessage(R.string.dialog_text_reg_event_question)
                .setPositiveButton(
                        R.string.dialog_text_reg_event_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.registerEvent();
                            }
                        }
                ).setNegativeButton(
                R.string.dialog_text_reg_event_negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );
        return builder.create();
   }
}
