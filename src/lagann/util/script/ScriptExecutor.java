/*
 * This file is part of ScriptExecutor.
 * 
 * ScriptExecutor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ScriptExecutor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ScriptExecutor. If not, see <http://www.gnu.org/licenses/>.
 */

package lagann.util.script;

import java.io.DataOutputStream;
import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ScriptExecutor extends Activity {
  static final String TAG = "ScriptExecutor";

  static final String SHELL_COMMAND = "sh";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      Intent intent = getIntent();
      Uri uri = intent.getData();

      if(ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
        File file = new File(uri.getPath());
        if(file.exists() && file.canRead() && file.isFile()) {
          Process p;
          try {
            p = Runtime.getRuntime().exec("su");

            String command = SHELL_COMMAND + " " + file.getAbsolutePath() + "\n";

            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command);

            os.flush();

            setResult(Activity.RESULT_OK);
            Log.d(TAG, "Executed " + command);
            return;
          } catch (Exception e) {
          }
        }
      }

      Log.d(TAG, "Error executing command");
      setResult(Activity.RESULT_CANCELED);
    } finally {
      finish();
      overridePendingTransition(0, 0);
    }
  }

}
