#include <iostream>
#include <string>
#include <fstream>
#include <vector>

using namespace std;

/**
 * 切分字符串
 * @param str
 * @param delim
 * @return
 */
vector<string> split(const string &str, const string &delim) {
    vector<string> res;
    if (str.empty()) return res;
    char *strs = new char[str.length() + 1];
    strcpy(strs, str.c_str());

    char *d = new char[delim.length() + 1];
    strcpy(d, delim.c_str());

    char *p = strtok(strs, d);
    while (p) {
        string s = p;
        res.push_back(s);
        p = strtok(nullptr, d);
    }
    return res;
}

static string get_white_list(char *config_path) {
    std::ifstream conf_path;
    //文件读取，把原来的ios::out改成ios::in
    conf_path.open(config_path, std::ios::in);
    string result;

    string s;
    //一直读到文件末尾
    while (conf_path >> s) {
        result.append(s);
    }
//    while (getline(conf_path, buff)) {
//        cout << buff << endl;
//        result.append(buff);
//    }
//
    int i = result.find("tb_white_list=");
    result = result.substr(i + 14, result.length());
    conf_path.close();
    return result;
}

int main() {
    std::cout << "Hello, World!" << std::endl;

    char *config_path = "/Users/huenhui/IdeaProjects/wealth/wealth-data-node/wealth-data-node-oceanbase/kafka-cpp/resource/oblog.conf";
    string white_list = get_white_list(config_path);
    std::cout << white_list << std::endl;

    const std::vector<string> &tables = split(white_list, "|");

    for (const string &t: tables) {
        const std::vector<string> &info = split(t, ".");
        std::string name = info[0];
        std::string schema = info[1];
        std::string tableName = info[2];

        std::cout << name + "--" + schema + + "--" + tableName << std::endl;
    }

    return 0;
}

