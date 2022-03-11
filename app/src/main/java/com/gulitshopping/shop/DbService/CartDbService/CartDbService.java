package com.gulitshopping.shop.DbService.CartDbService;

import android.content.Context;

import com.gulitshopping.shop.CartMessage;
import com.gulitshopping.shop.CartMessage_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class CartDbService {
    private static CartDbService cartDbService;
    private final Context mContext;

    private final Box<CartMessage> cartmessagesBox;

    private CartDbService(Context context, BoxStore objectbox) {
        this.mContext = context.getApplicationContext();
        this.cartmessagesBox=objectbox.boxFor(CartMessage.class);
    }

    public static CartDbService getInstance(Context context, BoxStore objectbox) {
        if (cartDbService == null) {
            cartDbService = new CartDbService(context, objectbox);
        }
        return cartDbService;
    }

    public CartMessage filtedCartData(String id) {
        QueryBuilder<CartMessage> builder = cartmessagesBox.query().equal(CartMessage_.mkey,
                id);
        CartMessage filtereddata = builder.build().findFirst();
        return filtereddata;
    }

        public List<CartMessage> getAll(){
        List<CartMessage> messages =cartmessagesBox.getAll();
        System.out.println("get cart data"+messages.size());
        return messages;
    }
    public boolean storeCart(CartMessage payload) {
        cartmessagesBox.put(payload);
        System.out.println("put cart data");
        return true;
    }

    public boolean removecart(String cartMessage) {
        CartMessage localCache = getBookingByID(cartMessage);
        cartmessagesBox.remove(localCache.get_id());
        return true;
    }
    /**
     * Removes a farmer matching a particular offline ID
     *
     * @param id unique farmer id
     * @return remove status
     */
    public boolean removeBookingByID(String id){
        CartMessage localCache = getBookingByID(id);
        if(localCache != null){
            cartmessagesBox.remove(localCache.get_id());
            return true;
        }

        return false;
    }
    public CartMessage getBookingByID(String id){
        return cartmessagesBox.query().equal(CartMessage_.mkey, id).build().findFirst();
    }

    public boolean existsInCache(String id){
        return  cartmessagesBox.query().equal(CartMessage_.mkey, id).build().find().size() > 0;
    }

}
