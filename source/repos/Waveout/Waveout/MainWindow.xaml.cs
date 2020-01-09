using System;
using System.Windows;
using Microsoft.Win32;

namespace Waveout
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class MainWindow : Window
    {
        private WaveOut m_Player;
        private WaveFormat m_Format;
        private WaveOutStream m_AudioStream;
        private bool isPlaying;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void Play_Click(object sender, RoutedEventArgs e)
        {
            Stop_Click(null, null);
            if (m_AudioStream != null)
            {
                m_AudioStream.Position = 0;
                m_Player = new WaveOut(-1, m_Format, m_Format.nAvgBytesPerSec, 20, new BufferFillEventHandler(Filler));
                EnableButtons(false, false, true, true);
                isPlaying = true;
            }
        }
        private void Filler(IntPtr data, int size)
        {
            byte[] b = new byte[size];
            if (m_AudioStream != null)
            {
                int pos = 0;
                while (pos < size)
                {
                    int toget = size - pos;
                    int got = m_AudioStream.Read(b, pos, toget);
                    if (got < toget)
                        m_AudioStream.Position = 0; // loop if the file ends
                    pos += got;
                }
            }
            else
            {
                for (int i = 0; i < b.Length; i++)
                    b[i] = 0;
            }
            System.Runtime.InteropServices.Marshal.Copy(b, 0, data, size);
        }
        private void Stop_Click(object sender, RoutedEventArgs e)
        {
            if (m_Player != null)
            {
                try
                {
                    m_Player.Dispose();
                }
                finally
                {
                    m_Player = null;
                    EnableButtons(true, true);
                }
            }
        }

        private void Open_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            if (openFileDialog.ShowDialog() == true)
            {
                CloseFile();
                try
                {
                    WaveOutStream S = new WaveOutStream(openFileDialog.FileName);
                    if (S.Length <= 0)
                        throw new Exception("Invalid WAV file");
                    m_Format = S.Format;
                    if (m_Format.wFormatTag != (short)WaveFormats.Pcm && m_Format.wFormatTag != (short)WaveFormats.Float)
                        throw new Exception("Olny PCM files are supported");

                    m_AudioStream = S;
                    textBoxPlayFilePath.DataContext = openFileDialog.SafeFileName;
                    EnableButtons(true, true);
                }
                catch (Exception exp)
                {
                    CloseFile();
                    MessageBox.Show(exp.Message);
                    textBoxPlayFilePath.DataContext = "Selected file is invalid";
                    EnableButtons(true);
                }
            }
        }

        private void Pause_Click(object sender, RoutedEventArgs e)
        {
            if (isPlaying)
            {
                m_Player.Pause();
                Pause.DataContext = "Resume";
            }
            else
            {
                m_Player.Resume();
                Pause.DataContext = "Pause";
            }
            isPlaying = !isPlaying;
        }

        private void CloseFile()
        {
            Stop_Click(null, null);
            if (m_AudioStream != null)
                try
                {
                    m_AudioStream.Close();
                }
                finally
                {
                    m_AudioStream = null;
                }
        }
        private void EnableButtons(bool open = false, bool play = false, bool pause_or_resume = false, bool stop = false)
        {
            Open.IsEnabled = open;
            Play.IsEnabled = play;
            Pause.IsEnabled = pause_or_resume;
            Stop.IsEnabled = stop;
        }
    }
}
