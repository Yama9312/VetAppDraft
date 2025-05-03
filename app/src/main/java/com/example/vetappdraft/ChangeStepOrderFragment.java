package com.example.vetappdraft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

/**
 * Fragment to handle changing the order of steps.
 * Converted from previous BaseActivity implementation.
 *
 * @author ania
 */
public class ChangeStepOrderFragment extends Fragment {
    private Button mBtnApply;
    private ImageButton mBtnGoBack;

    private LinkedList<Page> theSteps;
    private RecyclerView rvOrder;
    private StepAdapter mcAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_step_order, container, false);

        mBtnGoBack = view.findViewById(R.id.imBtnGoBack);
        rvOrder = view.findViewById(R.id.rvOrder);  // Ensure your layout has this ID

        // back button click implementation (return to the previous fragment)
        mBtnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        theSteps = new LinkedList<>();
        theSteps.add(new Page("Step1", Page.PageType.TEXT, "take a deep breath", ""));
        theSteps.add(new Page("Step2", Page.PageType.TEXT, "open the package", ""));
        theSteps.add(new Page("Step3", Page.PageType.TEXT, "do the procedure", ""));

        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        mcAdapter = new StepAdapter(theSteps);
        rvOrder.setAdapter(mcAdapter);

        // Drag-and-drop support
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(theSteps, fromPosition, toPosition);
                mcAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //unsure
            }
        });

        itemTouchHelper.attachToRecyclerView(rvOrder);

        return view;
    }
}
