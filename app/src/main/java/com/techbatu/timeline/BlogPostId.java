package com.techbatu.timeline;


import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class BlogPostId {

    @Exclude

    public String BlogPostId;

        public <T extends BlogPostId> T withId (@NonNull final String Id)
        {
            this.BlogPostId = Id;
            return (T) this;
        }

}
