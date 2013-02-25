package com.yudiz.Message;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;


public final class SmileyParser {

	private static SmileyParser sInstance;

	public static SmileyParser getInstance(final Context context) {
		if (sInstance == null) {
			sInstance = new SmileyParser(context);
		}
		return sInstance;
	}

	private final Context mContext;

	private final String[] mSmileyTexts;

	private final Pattern mPattern;

	private final HashMap<String, Integer> mSmileyToRes;

	private SmileyParser(final Context context) {
		this.mContext = context;
		this.mSmileyTexts = this.mContext.getResources().getStringArray(
				R.array.emoticons);
		this.mSmileyToRes = this.buildSmileyToRes();
		this.mPattern = this.buildPattern();
	}

	public static final int[] DEFAULT_SMILEY_RES_IDS = {
			R.drawable.angry, 
			R.drawable.confused, 
			R.drawable.cool, 
			R.drawable.cry, 
			R.drawable.kiss, 
			R.drawable.money,
			R.drawable.sad,
			R.drawable.smile,
			R.drawable.tongue,
			R.drawable.wink,
			R.drawable.wub,
	};
	private HashMap<String, Integer> buildSmileyToRes() {
		if (DEFAULT_SMILEY_RES_IDS.length != this.mSmileyTexts.length) {
			throw new IllegalStateException("Smiley resource ID/text mismatch");
		}
		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(
				this.mSmileyTexts.length);
		for (int i = 0; i < this.mSmileyTexts.length; i++) {
			smileyToRes.put(this.mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
		}
		return smileyToRes;
	}

	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(
				this.mSmileyTexts.length * 3);
		patternString.append('(');
		for (String s : this.mSmileyTexts) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1, patternString
				.length(), ")");
		return Pattern.compile(patternString.toString());
	}

	public CharSequence addSmileySpans(final CharSequence text) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		Matcher matcher = this.mPattern.matcher(text);
		while (matcher.find()) {
			int resId = this.mSmileyToRes.get(matcher.group());
			builder.setSpan(new ImageSpan(this.mContext, resId), matcher
					.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}
