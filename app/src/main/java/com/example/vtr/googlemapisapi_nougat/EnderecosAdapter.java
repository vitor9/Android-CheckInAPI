package com.example.vtr.googlemapisapi_nougat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EnderecosAdapter extends BaseAdapter{

    Context context;
    List<Endereco> enderecos;

    public EnderecosAdapter(Context context, List<Endereco> enderecos) {
        this.context = context;
        this.enderecos = enderecos;
    }

    @Override
    public int getCount() {
        return this.enderecos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.enderecos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);

        Endereco endereco = this.enderecos.get(position);

        View v  = layoutInflater.inflate(R.layout.listview_endereco, null);

        TextView txtEndereco = v.findViewById(R.id.txtEndereco);

        txtEndereco.setText( endereco.getDescricao() );

        return v;
    }
}
