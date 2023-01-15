package com.atrvasa.infra;


public class DateLoader {
    public int year, month, day, hour, minute, second;

    public DateLoader(String date) {
        this.loadDate(date);
    }

    private void loadDate(String strDate) {
        strDate = strDate.replace('-', '/').trim();
        String date = "";
        int index = 0;
        for (int i = 0; i < strDate.length() &&
                strDate.charAt(i) != ' '; i++) {
            date = date + strDate.charAt(i);
            index++;
        }
        String time = "";
        if (strDate.length() > 11)
            time = strDate.substring(index);

        String[] date_splited = date.split("/");
        if (!StringTools.isNullOrEmpty(date_splited[0]))
            this.year = Integer.parseInt(date_splited[0]);
        if (!StringTools.isNullOrEmpty(date_splited[1]))
            this.month = Integer.parseInt(date_splited[1]);
        if (!StringTools.isNullOrEmpty(date_splited[2]))
            this.day = Integer.parseInt(date_splited[2]);

        String[] time_splited = time.split(":");
        if (time_splited.length > 0 && !StringTools.isNullOrEmpty(time_splited[0]))
            this.hour = Integer.parseInt(time_splited[0].trim());
        if (time_splited.length > 1 && !StringTools.isNullOrEmpty(time_splited[1]))
            this.minute = Integer.parseInt(time_splited[1].trim());
        if (time_splited.length > 2 && !StringTools.isNullOrEmpty(time_splited[2]))
            this.second = Integer.parseInt(time_splited[2].trim());
    }

    @Override
    public String toString() {
        return "DateLoader{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }


    public static int[] gregorian_to_jalali(int gy, int gm, int gd) {
        int[] out = {
                (gm > 2) ? (gy + 1) : gy,
                0,
                0
        };
        {
            int[] g_d_m = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
            out[2] = 355666 + (365 * gy) + ((int) ((out[0] + 3) / 4)) - ((int) ((out[0] + 99) / 100))
                    + ((int) ((out[0] + 399) / 400)) + gd + g_d_m[gm - 1];
        }
        out[0] = -1595 + (33 * ((int) (out[2] / 12053)));
        out[2] %= 12053;
        out[0] += 4 * ((int) (out[2] / 1461));
        out[2] %= 1461;
        if (out[2] > 365) {
            out[0] += (int) ((out[2] - 1) / 365);
            out[2] = (out[2] - 1) % 365;
        }
        if (out[2] < 186) {
            out[1] = 1 + (int) (out[2] / 31);
            out[2] = 1 + (out[2] % 31);
        } else {
            out[1] = 7 + (int) ((out[2] - 186) / 30);
            out[2] = 1 + ((out[2] - 186) % 30);
        }
        return out;
    }

    public static int[] jalali_to_gregorian(int jy, int jm, int jd) {
        jy += 1595;
        int[] out = {
                0,
                0,
                -355668 + (365 * jy) + (((int) (jy / 33)) * 8) + ((int) (((jy % 33) + 3) / 4)) + jd + (
                        (jm < 7) ? (jm - 1) * 31 : ((jm - 7) * 30) + 186)
        };
        out[0] = 400 * ((int) (out[2] / 146097));
        out[2] %= 146097;
        if (out[2] > 36524) {
            out[0] += 100 * ((int) (--out[2] / 36524));
            out[2] %= 36524;
            if (out[2] >= 365) {
                out[2]++;
            }
        }
        out[0] += 4 * ((int) (out[2] / 1461));
        out[2] %= 1461;
        if (out[2] > 365) {
            out[0] += (int) ((out[2] - 1) / 365);
            out[2] = (out[2] - 1) % 365;
        }
        int[] sal_a = {0, 31, ((out[0] % 4 == 0 && out[0] % 100 != 0) || (out[0] % 400 == 0)) ? 29 : 28,
                31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (out[2]++; out[1] < 13 && out[2] > sal_a[out[1]]; out[1]++) {
            out[2] -= sal_a[out[1]];
        }
        return out;
    }
}
