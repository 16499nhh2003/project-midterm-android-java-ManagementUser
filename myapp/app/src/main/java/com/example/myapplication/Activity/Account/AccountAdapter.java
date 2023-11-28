package com.example.myapplication.Activity.Account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
//hihi

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    static List<Account> accounts;
    static Context context;

    public AccountAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_account, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnClickLister(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View v, int p) {
            }

            @Override
            public void onItemLongClick(View v, int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                String[] options = {"Update", "Delete"};
                alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String id = accounts.get(position).getId();
                            String name = accounts.get(position).getName();
                            boolean status = accounts.get(position).isStatus();
                            String phone = accounts.get(position).getPhoneNumber();
                            int age = accounts.get(position).getAge();

                            Intent intent = new Intent(parent.getContext(), EditAccountActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("status", status);
                            intent.putExtra("phone", phone);
                            intent.putExtra("age", age);

                            ((AccountActivity) context).startActivityForResult(intent, Constaint.KEY_ADD_UPDATE_ACCOUNT);
                        }
                        if (which == 1) {
                            ((AccountActivity) context).deleteData(position);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
        holder.bindData(position, accounts.get(position));
    }

    @Override
    public int getItemCount() {
        if (accounts == null) {
            return 0;
        }
        return accounts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView etName, statusAccount, role, phone, age;
        public Button his;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = (TextView) itemView.findViewById(R.id.etName);
            statusAccount = (TextView) itemView.findViewById(R.id.statusAccount);
            phone = (TextView) itemView.findViewById(R.id.tvPhoneUser);
            age = (TextView) itemView.findViewById(R.id.tvAgeUser);
            role = (TextView) itemView.findViewById(R.id.tvRoleUser);
            his = (Button) itemView.findViewById(R.id.btnHis);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _clickListener.onItemClick(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    _clickListener.onItemLongClick(v, getAdapterPosition());
                    return false;
                }
            });

        }

        public void bindData(int p, Account account) {
            etName.setText(account.getName());
            statusAccount.setText((!account.isStatus() ? "Locked" : "Normal"));
            phone.setText(account.getPhoneNumber());
            age.setText(account.getAge() + "");
            role.setText(account.getRole());

            his.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(AccountAdapter.class.getSimpleName(), accounts.get(getAdapterPosition()).toString());

                    Intent intent = new Intent(((AccountActivity) context), HistoryActivity.class);
                    intent.putExtra("uid", account.getId());
                    ((AccountActivity) context).startActivity(intent);

                }
            });
        }

        private ViewHolder.ClickListener _clickListener;

        public interface ClickListener {
            void onItemClick(View v, int p);

            void onItemLongClick(View v, int p);
        }

        public void setOnClickLister(ViewHolder.ClickListener clickListener) {
            _clickListener = clickListener;
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        this.notifyDataSetChanged();
    }
}