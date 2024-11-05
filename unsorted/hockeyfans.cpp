#include <iostream>
#include <vector>
#include <set>

using namespace std;

bool isValid(vector<int> chants, int i, int s, int temp) {
    for (int j = i; j < i+s; j++) {
        if (chants[j] < temp) {
            return false;
        }
    }
    return true;
}

int main() {
    int n,s,m;
    cin >> n >> s >> m;

    vector<int> chants;
    set<int> values;

    for (int i = 0; i < n; i++) {
        int volume;
        cin >> volume;
        chants.push_back(volume);
        values.insert(volume);
    }

    vector<int> valuesList(values.begin(), values.end());
    int a = 0, b = valuesList.size();

    while(true) {
        int mid = (a + b) / 2;
        int count = 0;

        for (int i = 0; i <= (n - s);) {
        if (isValid(chants, i, s, valuesList[mid])) {
            count++;
            i += s;
        } else {
            i++;
        }
        }

        if (count >= m) {
            a = mid;
        } else {
            b = mid;
        }

        if (abs(b - a) <= 1) {
            break;
        }
    }

    cout << valuesList[a] << "\n";

    return 0;
}